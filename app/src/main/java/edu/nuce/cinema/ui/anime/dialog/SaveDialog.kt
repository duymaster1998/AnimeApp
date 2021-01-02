package edu.nuce.cinema.ui.anime.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import edu.nuce.base.delegates.viewBinding
import edu.nuce.base.extensions.toast
import edu.nuce.base.mvi.MviView
import edu.nuce.base.view.BaseDialogFragment
import edu.nuce.cinema.R
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.Archive
import edu.nuce.cinema.data.models.Series
import edu.nuce.cinema.data.models.request.StorageParams
import edu.nuce.cinema.databinding.DialogSaveBinding
import edu.nuce.cinema.ui.anime.AnimeEvents
import edu.nuce.cinema.ui.common.constants.TypeSeries
import edu.nuce.cinema.ui.library.LibraryEvents
import edu.nuce.cinema.ui.library.adapter.ArchiveAdapter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber

@AndroidEntryPoint
class SaveDialog(
    private val archives: List<Int>,
    private val episode: Int,
    private val type: String,
    private val series: Series?,
    private val event:AnimeEvents
) : BaseDialogFragment(), LibraryEvents,
    MviView<SaveIntent, SaveViewState> {

    private val _binding by viewBinding<DialogSaveBinding>()
    private lateinit var adaperArchive: ArchiveAdapter

    private val _getSaveListIntentPublisher =
        PublishSubject.create<SaveIntent.GetListSaveIntent>()
    private val _addSaveListIntentPublisher =
        PublishSubject.create<SaveIntent.AddListSaveIntent>()
    private val _storageIntentPublisher =
        PublishSubject.create<SaveIntent.StorageIntent>()
    private val _viewModel: SaveViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_save, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adaperArchive = ArchiveAdapter(this)
        bind()
        _getSaveListIntentPublisher.onNext(SaveIntent.GetListSaveIntent)
    }

    private fun bind() {
        _viewModel.states().subscribe(this::render).disposeOnDestroy()
        _viewModel.processIntents(intents())
    }

    override fun intents(): Observable<SaveIntent> {
        return Observable.merge(
            _getSaveListIntentPublisher,
            _addSaveListIntentPublisher,
            _storageIntentPublisher
        )
    }

    override fun render(state: SaveViewState) {
        if (state.error != null) {
            Timber.e(state.error)
            return
        }
        Timber.e(state.isLoading.toString())
        adaperArchive.submitList(state.archives)
        _binding.apply {
            btnClose.setOnClickListener{ dialog?.dismiss()}
            btnSubmit.setOnClickListener {
                if (etArchive.text.isNotEmpty()) {
                    addArchive(etArchive.text.toString())
//                    dialog?.dismiss()
                    etArchive.text.clear()
                    toast("Add Archive Success")
                }
            }
            rvArchive.adapter = adaperArchive
        }
    }

    fun checkExist(archive: Archive): Boolean {
        if(archives.size == 0)
            return true
        archives.forEach { item ->
            if (item == archive.id)
                return false
        }
        return true
    }
    override fun onClickArchive(archive: Archive) {
        if (checkExist(archive) == true) {
            _storageIntentPublisher.onNext(
                SaveIntent.StorageIntent(
                    StorageParams(
                        archiveId = archive.id,
                        episode, seriesId = series!!.id, type
                    )
                )
            )
            toast("Save Success")
            event.onSubmit()
            dialog?.dismiss()
        } else if(checkExist(archive) == false)
            toast("Item Exists")
    }

    override fun onLongClickArchive(archive: Archive):Boolean {
        return false
    }

    override fun addArchive(name: String) {
        _addSaveListIntentPublisher.onNext(SaveIntent.AddListSaveIntent(name))
    }

    override fun onDeleteArchive(input: Archive) {
        toast("none")
    }
}