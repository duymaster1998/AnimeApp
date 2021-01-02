package edu.nuce.cinema.ui.manga

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.krishna.fileloader.FileLoader
import com.krishna.fileloader.listener.FileRequestListener
import com.krishna.fileloader.pojo.FileResponse
import com.krishna.fileloader.request.FileLoadRequest
import dagger.hilt.android.AndroidEntryPoint
import edu.nuce.base.auth.RxOAuthManager
import edu.nuce.base.delegates.viewBinding
import edu.nuce.base.extensions.onBackPressed
import edu.nuce.base.extensions.toast
import edu.nuce.base.mvi.MviView
import edu.nuce.cinema.R
import edu.nuce.cinema.data.models.request.HistoryParams
import edu.nuce.cinema.databinding.FragmentMangaBinding
import edu.nuce.cinema.ui.anime.AnimeEvents
import edu.nuce.cinema.ui.anime.dialog.SaveDialog
import edu.nuce.cinema.ui.base.BaseFragment
import edu.nuce.cinema.ui.common.constants.Base
import edu.nuce.cinema.ui.common.constants.TypeSeries
import edu.nuce.cinema.ui.manga.MangaFragmentDirections.Companion.actionMangaToCommentManga
import edu.nuce.cinema.ui.manga.MangaFragmentDirections.Companion.actionMangaToLogin
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class MangaFragment : BaseFragment(R.layout.fragment_manga), MviView<MangaIntent, MangaViewState>,
    AnimeEvents {

    private val args by navArgs<MangaFragmentArgs>()
    private val _binding by viewBinding<FragmentMangaBinding>()
    lateinit var saveDialog: SaveDialog

    @Inject
    lateinit var requestManager: RequestManager

    @Inject
    lateinit var rxOAuthManager: RxOAuthManager
    private var isCheck: Boolean = true
    private val _getSeriesItentPublisher =
        PublishSubject.create<MangaIntent.GetSeriesIntent>()
    private val _updateViewItentPublisher =
        PublishSubject.create<MangaIntent.UpdateMangaIntent>()
    private val _getArchiveItentPublisher =
        PublishSubject.create<MangaIntent.GetArchiveIntent>()
    private val _viewModel: MangaViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        if (rxOAuthManager.isExistsCredentials)
            _getSeriesItentPublisher.onNext(MangaIntent.GetSeriesIntent(args.manga.id))
        _getArchiveItentPublisher.onNext(MangaIntent.GetArchiveIntent(args.manga.id))
        _updateViewItentPublisher.onNext(
            MangaIntent.UpdateMangaIntent(
                args.manga.id,
                HistoryParams(args.manga.episode, seriesId = args.manga.id, TypeSeries.MANGA)
            )
        )
        _binding.loading.visibility = View.VISIBLE
        FileLoader.with(requireContext())
            .load(Base.URL_IMAGE + args.manga.file, true)
            .fromDirectory("PDFFile", FileLoader.DIR_INTERNAL)
            .asFile(object : FileRequestListener<File> {
                override fun onLoad(request: FileLoadRequest?, response: FileResponse<File?>?) {
                    _binding.loading.visibility = View.GONE
                    val pdfFile = response!!.body
                    _binding.pdfView.fromFile(pdfFile)
                        .defaultPage(0)
                        .swipeHorizontal(true)
                        .pageSnap(true)
                        .autoSpacing(true)
                        .pageFling(true)
                        .pageFitPolicy(FitPolicy.HEIGHT)
                        .load()

                }

                override fun onError(request: FileLoadRequest?, t: Throwable?) {
                    Timber.e(t.toString())
                    _binding.loading.visibility = View.GONE
                }

            })
    }

    private fun bind() {
        _viewModel.states().subscribe(this::render).disposeOnDestroy()
        _viewModel.processIntents(intents())
    }

    override fun intents(): Observable<MangaIntent> {
        return Observable.merge(
            _getSeriesItentPublisher,
            _updateViewItentPublisher,
            _getArchiveItentPublisher
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun render(state: MangaViewState) {
        if (state.error != null) {
            Timber.e(state.error)
            return
        }
        Timber.e(state.isLoading.toString())
        Timber.e(state.archives.toString())
        _binding.apply {
            ivMess.setOnClickListener { findNavController().navigate(actionMangaToCommentManga(args.manga)) }
            pdfView.setOnClickListener { check(isCheck) }
            ivAction.setOnClickListener { onBackPressed() }
            ivSave.setOnClickListener {
                saveDialog =
                    SaveDialog(
                        state.archives,
                        args.manga.episode,
                        TypeSeries.MANGA,
                        state.series,
                        this@MangaFragment
                    )
                if (rxOAuthManager.isExistsCredentials)
                    this@MangaFragment.fragmentManager?.let { it ->
                        saveDialog.show(
                            it,
                            "name"
                        )
                    }
                else
                    findNavController().navigate(actionMangaToLogin())
            }
        }
    }

    fun check(state: Boolean) {
        if (state == true) {
            _binding.toolbar.visibility = View.VISIBLE
            isCheck = false
        } else {
            _binding.toolbar.visibility = View.INVISIBLE
            isCheck = true
        }
    }

    override fun onSubmit() {
        _getArchiveItentPublisher.onNext(MangaIntent.GetArchiveIntent(args.manga.id))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        try {
            val file = File(requireContext().filesDir,args.manga.file)
            file.delete()
        }catch (e:Exception){
            toast(e.toString())
        }
    }
}
