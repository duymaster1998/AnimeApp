package edu.nuce.cinema.ui.anime

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import edu.nuce.base.delegates.viewBinding
import edu.nuce.base.extensions.onBackPressed
import edu.nuce.base.mvi.MviView
import edu.nuce.cinema.R
import edu.nuce.cinema.databinding.FragmentAnimeBinding
import edu.nuce.cinema.ui.base.BaseFragment
import edu.nuce.cinema.ui.manga.adapter.AdapterCategorySeries
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AnimeFragment : BaseFragment(R.layout.fragment_anime), MviView<AnimeIntent, AnimeViewState> {
    private val _binding: FragmentAnimeBinding by viewBinding()
    private val args by navArgs<AnimeFragmentArgs>()
    private lateinit var adapterCategorySeries: AdapterCategorySeries

    private val _getCategoryBySeriesItentPublisher =
        PublishSubject.create<AnimeIntent.GetCategoryBySeriesIntent>()
    private val _getRateSeriesItentPublisher =
        PublishSubject.create<AnimeIntent.GetRateSeriesIntent>()
    private val _getSeriesItentPublisher =
        PublishSubject.create<AnimeIntent.GetSeriesIntent>()
    private val _getSeriesAItentPublisher =
        PublishSubject.create<AnimeIntent.GetSeriesAIntent>()
    private val _viewModel: AnimeViewModel by viewModels()

    @Inject
    lateinit var requestManager: RequestManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        if(args.isSeries)
            _getSeriesItentPublisher.onNext(AnimeIntent.GetSeriesIntent(args.id))
        else{
            _getSeriesAItentPublisher.onNext(AnimeIntent.GetSeriesAIntent(args.id))
        }
    }

    private fun bind() {
        _viewModel.states().subscribe(this::render).disposeOnDestroy()
        _viewModel.processIntents(intents())
    }

    override fun intents(): Observable<AnimeIntent> {
        return Observable.merge(
            _getCategoryBySeriesItentPublisher,
            _getRateSeriesItentPublisher,
            _getSeriesItentPublisher,
            _getSeriesAItentPublisher
        )
    }

    override fun render(state: AnimeViewState) {
        if (state.error != null) {
            Timber.e(state.error)
            return
        }
        Timber.e(state.isLoading.toString())
        state.series.let {
            _getCategoryBySeriesItentPublisher.onNext(AnimeIntent.GetCategoryBySeriesIntent(it!!.id))
            _getRateSeriesItentPublisher.onNext(AnimeIntent.GetRateSeriesIntent(it.id))
        }
        adapterCategorySeries = AdapterCategorySeries(requestManager)
        adapterCategorySeries.submitList(state.categories)
        _binding.run {
            description.apply {
                rvCategory.adapter = adapterCategorySeries
                tvName.text  = state.series!!.name
                layout.layoutParams.width = requireActivity().windowManager.defaultDisplay.width
                content.text = state.series.sumary
            }
            chapters.apply {
                layout.layoutParams.width = requireActivity().windowManager.defaultDisplay.width
            }
            requestManager.load(state.series!!.backdropPath).into(image)
            include.imgAction.setOnClickListener { onBackPressed() }
        }
    }
}