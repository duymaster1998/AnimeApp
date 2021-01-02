package edu.nuce.cinema.ui.chapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import edu.nuce.base.delegates.viewBinding
import edu.nuce.base.extensions.onBackPressed
import edu.nuce.base.extensions.toast
import edu.nuce.base.mvi.MviView
import edu.nuce.cinema.R
import edu.nuce.cinema.data.models.Manga
import edu.nuce.cinema.databinding.FragmentChapterBinding
import edu.nuce.cinema.ui.base.BaseFragment
import edu.nuce.cinema.ui.chapter.ChapterFragmentDirections.Companion.actionChapterToManga
import edu.nuce.cinema.ui.chapter.adapter.AdapterChapter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ChapterFragment : BaseFragment(R.layout.fragment_chapter),
    MviView<ChapterIntent, ChapterViewState>, AdapterChapter.MangaOnClick {

    private lateinit var adapter: AdapterChapter
    @Inject
    lateinit var requestManager: RequestManager
    private val args by navArgs<ChapterFragmentArgs>()
    private val _binding: FragmentChapterBinding by viewBinding()
    private val _getMangasIntentPublisher =
        PublishSubject.create<ChapterIntent.GetChapterMangaIntent>()
    private val _updateViewMangaIntentPublisher =
        PublishSubject.create<ChapterIntent.GetChapterMangaIntent>()

    private val _viewModel: ChapterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        _getMangasIntentPublisher.onNext(ChapterIntent.GetChapterMangaIntent(args.season.id))
    }

    override fun intents(): Observable<ChapterIntent> {
        return Observable.merge(
            _getMangasIntentPublisher,
            _updateViewMangaIntentPublisher
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun render(state: ChapterViewState) {
        if (state.error != null) {
            Timber.e(state.error)
            return
        }
        Timber.e(state.isLoading.toString())
        adapter = AdapterChapter(this)
        adapter.submitList(state.mangas)
        val seasonName = "Season "+args.season.name+": "+ args.season.title
        _binding.run {
            season.text = seasonName
            rvChapter.adapter = adapter
            appbar.imgAction.setImageDrawable(root.resources.getDrawable(R.drawable.ic_previous_black))
            appbar.title.text = root.resources.getString(R.string.chapter)
            appbar.imgAction.setOnClickListener { onBackPressed() }
        }
    }

    private fun bind() {
        _viewModel.states().subscribe(this::render).disposeOnDestroy()
        _viewModel.processIntents(intents())
    }

    override fun MangaOnClick(manga: Manga) {
        findNavController().navigate(actionChapterToManga(manga))
    }
}