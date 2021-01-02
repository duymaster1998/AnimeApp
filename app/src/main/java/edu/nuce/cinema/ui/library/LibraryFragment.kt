package edu.nuce.cinema.ui.library

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import edu.nuce.base.auth.RxOAuthManager
import edu.nuce.base.delegates.viewBinding
import edu.nuce.base.mvi.MviView
import edu.nuce.cinema.R
import edu.nuce.cinema.data.models.Archive
import edu.nuce.cinema.data.models.Series
import edu.nuce.cinema.databinding.FragmentLibraryBinding
import edu.nuce.cinema.ui.base.BaseFragment
import edu.nuce.cinema.ui.common.adapter.AdapterSeries
import edu.nuce.cinema.ui.common.constants.ActionLibrary
import edu.nuce.cinema.ui.detail.DetailFragmentDirections
import edu.nuce.cinema.ui.dialog.ArchiveDialog
import edu.nuce.cinema.ui.library.LibraryFragmentDirections.Companion.actionLibraryToDetail
import edu.nuce.cinema.ui.library.LibraryFragmentDirections.Companion.actionLibraryToLibraryDetail
import edu.nuce.cinema.ui.library.LibraryFragmentDirections.Companion.actionLibraryToLogin
import edu.nuce.cinema.ui.library.adapter.ArchiveAdapter
import edu.nuce.cinema.ui.library.dialog.ActionDialog
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LibraryFragment : BaseFragment(R.layout.fragment_library),
    MviView<LibraryIntent, LibraryViewState>, AdapterSeries.SeriesOnClick,
    LibraryEvents {

    private lateinit var adaperSubscribeSeries: AdapterSeries
    private lateinit var adaperArchive: ArchiveAdapter

    @Inject
    lateinit var requestManager: RequestManager

    @Inject
    lateinit var rxOAuthManager: RxOAuthManager
    lateinit var addArchiveDialog: ArchiveDialog
    lateinit var actionDialog: ActionDialog
    private val _binding: FragmentLibraryBinding by viewBinding()
    private val _getSubscribeSeriesIntentPublisher =
        PublishSubject.create<LibraryIntent.GetSubscribeSeriesIntent>()
    private val _getSaveListIntentPublisher =
        PublishSubject.create<LibraryIntent.GetListSaveIntent>()
    private val _addSaveListIntentPublisher =
        PublishSubject.create<LibraryIntent.AddListSaveIntent>()
    private val _removeArchiveIntentPublisher =
        PublishSubject.create<LibraryIntent.RemoveArchiveIntent>()

    private val _viewModel: LibraryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adaperSubscribeSeries = AdapterSeries(
            this,
            requestManager,
            resources.getDimension(R.dimen._130sdp).toInt(),
            resources.getDimension(R.dimen._80sdp).toInt()
        )
        adaperArchive = ArchiveAdapter(this)
        bind()
        if(rxOAuthManager.isExistsCredentials){
            _getSubscribeSeriesIntentPublisher.onNext(LibraryIntent.GetSubscribeSeriesIntent)
            _getSaveListIntentPublisher.onNext(LibraryIntent.GetListSaveIntent)
        }
    }

    override fun intents(): Observable<LibraryIntent> {
        return Observable.merge(
            _getSubscribeSeriesIntentPublisher,
            _getSaveListIntentPublisher,
            _addSaveListIntentPublisher,
            _removeArchiveIntentPublisher
        )
    }

    override fun render(state: LibraryViewState) {
        if (state.error != null) {
            Timber.e(state.error)
            return
        }
        Timber.e(state.isLoading.toString())
        addArchiveDialog = ArchiveDialog(this)
        adaperSubscribeSeries.submitList(state.series)
        adaperArchive.submitList(state.archives)
        if (state.series.size == 0)
            _binding.tvSubNull.visibility = View.VISIBLE
        else
            _binding.tvSubNull.visibility = View.INVISIBLE
        _binding.run {
            rvSubcribe.adapter = adaperSubscribeSeries
            rvSaved.adapter = adaperArchive
            btnAddArchive.setOnClickListener {
                if (rxOAuthManager.isExistsCredentials)
                    this@LibraryFragment.fragmentManager?.let { it ->
                        addArchiveDialog.show(
                            it,
                            "name"
                        )
                    }
                else
                    findNavController().navigate(actionLibraryToLogin())
            }
            tvHistory.setOnClickListener {
                if (rxOAuthManager.isExistsCredentials)
                    findNavController().navigate(actionLibraryToLibraryDetail(0, ActionLibrary.HISTORY))
                else
                    findNavController().navigate(actionLibraryToLogin())
            }
        }
    }

    private fun bind() {
        _viewModel.states().subscribe(this::render).disposeOnDestroy()
        _viewModel.processIntents(intents())
    }

    override fun onClickSeries(view: View, series: Series) {
        findNavController().navigate(actionLibraryToDetail(series))
    }

    override fun onClickArchive(archive: Archive) {
        findNavController().navigate(
            actionLibraryToLibraryDetail(
                archive.id,
                ActionLibrary.SAVE_LIST
            )
        )
    }

    override fun onLongClickArchive(archive: Archive): Boolean {
        actionDialog = ActionDialog(this, archive)
        this@LibraryFragment.fragmentManager?.let { it ->
            actionDialog.show(
                it,
                "name"
            )
        }
        return true
    }


    override fun addArchive(name: String) {
        _addSaveListIntentPublisher.onNext(LibraryIntent.AddListSaveIntent(name))
    }

    override fun onDeleteArchive(input: Archive) {
        _removeArchiveIntentPublisher.onNext(LibraryIntent.RemoveArchiveIntent(input.id))
    }

}