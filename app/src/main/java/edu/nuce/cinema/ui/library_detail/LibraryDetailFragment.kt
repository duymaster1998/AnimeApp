package edu.nuce.cinema.ui.LibraryDetail_detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import edu.nuce.base.auth.RxOAuthManager
import edu.nuce.base.delegates.viewBinding
import edu.nuce.base.extensions.onBackPressed
import edu.nuce.base.mvi.MviView
import edu.nuce.cinema.R
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.Manga
import edu.nuce.cinema.databinding.FragmentDetailLibraryBinding
import edu.nuce.cinema.ui.LibraryDetail_detail.LibraryDetailFragmentDirections.Companion.actionLibraryDetailToManga
import edu.nuce.cinema.ui.base.BaseFragment
import edu.nuce.cinema.ui.chapter.adapter.AdapterChapter
import edu.nuce.cinema.ui.common.constants.ActionLibrary
import edu.nuce.cinema.ui.home.adapter.AdapterAnimeN
import edu.nuce.cinema.ui.library_detail.LibraryDetailIntent
import edu.nuce.cinema.ui.library_detail.LibraryDetailViewState
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LibraryDetailFragment : BaseFragment(R.layout.fragment_detail_library),
    MviView<LibraryDetailIntent, LibraryDetailViewState>, AdapterAnimeN.OnClickAnime,
    AdapterChapter.MangaOnClick {

    lateinit var adapterAnime: AdapterAnimeN
    lateinit var adapterManga: AdapterChapter

    @Inject
    lateinit var requestManager: RequestManager

    @Inject
    lateinit var rxOAuthManager: RxOAuthManager
    private val args by navArgs<LibraryDetailFragmentArgs>()
    private val _binding: FragmentDetailLibraryBinding by viewBinding()
    private val _getAnimeIntentPublisher =
        PublishSubject.create<LibraryDetailIntent.GetAnimeIntent>()
    private val _getMangaIntentPublisher =
        PublishSubject.create<LibraryDetailIntent.GetMangaIntent>()
    private val _getAnimeByArchiveIntentPublisher =
        PublishSubject.create<LibraryDetailIntent.GetAnimeByArchiveIntent>()
    private val _getMangaByArchiveIntentPublisher =
        PublishSubject.create<LibraryDetailIntent.GetMangaByArchiveIntent>()

    private val _viewModel: LibraryDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterAnime = AdapterAnimeN(this, requestManager)
        adapterManga = AdapterChapter(this)
        bind()
        when (args.type) {
            ActionLibrary.HISTORY -> {
                _getAnimeIntentPublisher.onNext(LibraryDetailIntent.GetAnimeIntent)
                _getMangaIntentPublisher.onNext(LibraryDetailIntent.GetMangaIntent)
                _binding.appbar.title.text = resources.getString(R.string.history)
            }
            ActionLibrary.SAVE_LIST -> {
                _getAnimeByArchiveIntentPublisher.onNext(LibraryDetailIntent.GetAnimeByArchiveIntent(args.id))
                _getMangaByArchiveIntentPublisher.onNext(LibraryDetailIntent.GetMangaByArchiveIntent(args.id))
                _binding.appbar.title.text = resources.getString(R.string.saved_list)
            }
        }

    }

    override fun intents(): Observable<LibraryDetailIntent> {
        return Observable.merge(
            _getAnimeIntentPublisher,
            _getMangaIntentPublisher,
            _getMangaByArchiveIntentPublisher,
            _getAnimeByArchiveIntentPublisher
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun render(state: LibraryDetailViewState) {
        if (state.error != null) {
            Timber.e(state.error)
            return
        }
        Timber.e(state.isLoading.toString())
        if (state.animes.size == 0)
            _binding.tvAnimeNull.visibility = View.VISIBLE
        else
            _binding.tvAnimeNull.visibility = View.INVISIBLE
        if (state.mangas.size == 0)
            _binding.tvMangaNull.visibility = View.VISIBLE
        else
            _binding.tvMangaNull.visibility = View.INVISIBLE
        adapterAnime.submitList(state.animes)
        adapterManga.submitList(state.mangas)
        _binding.apply {
            rvEpisode.adapter = adapterAnime
            rvChapter.adapter = adapterManga
            appbar.imgAction.setImageDrawable(root.resources.getDrawable(R.drawable.ic_previous_black))
            appbar.imgAction.setOnClickListener { onBackPressed() }
        }
    }

    private fun bind() {
        _viewModel.states().subscribe(this::render).disposeOnDestroy()
        _viewModel.processIntents(intents())
    }

    override fun onClickAnimeN(view: View, anime: Anime) {
        findNavController().navigate(
            LibraryDetailFragmentDirections.actionLibraryDetailToAnime(
                anime
            )
        )
    }

    override fun MangaOnClick(manga: Manga) {
        findNavController().navigate(actionLibraryDetailToManga(manga))
    }
}