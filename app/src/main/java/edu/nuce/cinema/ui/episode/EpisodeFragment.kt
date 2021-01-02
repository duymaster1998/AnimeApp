package edu.nuce.cinema.ui.episode

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
import edu.nuce.base.mvi.MviView
import edu.nuce.cinema.R
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.databinding.FragmentEpisodeBinding
import edu.nuce.cinema.ui.base.BaseFragment
import edu.nuce.cinema.ui.detail.adapter.AdapterEpisode
import edu.nuce.cinema.ui.episode.EpisodeFragmentDirections.Companion.actionEpisodeToAnime
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class EpisodeFragment : BaseFragment(R.layout.fragment_episode),
    MviView<EpisodeIntent, EpisodeViewState>, AdapterEpisode.AnimeOnClick {

    private lateinit var adapter: AdapterEpisode

    @Inject
    lateinit var requestManager: RequestManager
    private val args by navArgs<EpisodeFragmentArgs>()
    private val _binding: FragmentEpisodeBinding by viewBinding()
    private val _getAnimesIntentPublisher =
        PublishSubject.create<EpisodeIntent.GetEpisodeAnimeIntent>()
    private val _updateIntentPublisher =
        PublishSubject.create<EpisodeIntent.GetEpisodeAnimeIntent>()

    private val _viewModel: EpisodeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        _getAnimesIntentPublisher.onNext(EpisodeIntent.GetEpisodeAnimeIntent(args.season.id))
    }

    override fun intents(): Observable<EpisodeIntent> {
        return Observable.merge(
            _getAnimesIntentPublisher,
            _updateIntentPublisher
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun render(state: EpisodeViewState) {
        if (state.error != null) {
            Timber.e(state.error)
            return
        }
        Timber.e(state.isLoading.toString())
        adapter = AdapterEpisode(this, requestManager)
        adapter.submitList(state.animes)
        _binding.run {
            rvEpisode.adapter = adapter
            appbar.imgAction.setImageDrawable(root.resources.getDrawable(R.drawable.ic_previous_black))
            appbar.title.text = root.resources.getString(R.string.episode)
            appbar.imgAction.setOnClickListener { onBackPressed() }
        }
    }

    private fun bind() {
        _viewModel.states().subscribe(this::render).disposeOnDestroy()
        _viewModel.processIntents(intents())
    }

    override fun AnimeOnClick(anime: Anime) {
        findNavController().navigate(actionEpisodeToAnime(anime))
    }

}