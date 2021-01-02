package edu.nuce.cinema.ui.series

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import edu.nuce.base.delegates.viewBinding
import edu.nuce.base.extensions.onBackPressed
import edu.nuce.base.mvi.MviView
import edu.nuce.cinema.R
import edu.nuce.cinema.data.models.Series
import edu.nuce.cinema.databinding.FragmentSeriesBinding
import edu.nuce.cinema.ui.base.BaseFragment
import edu.nuce.cinema.ui.common.constants.SeriesTypeAction
import edu.nuce.cinema.ui.series.SeriesFragmentDirections.Companion.actionSeriesToDetail
import edu.nuce.cinema.ui.series.adapter.AdapterSeries2
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SeriesFragment : BaseFragment(R.layout.fragment_series),
    MviView<SeriesIntent, SeriesViewState>, AdapterSeries2.SeriesOnClick {

    lateinit var adapter: AdapterSeries2

    @Inject
    lateinit var requestManager: RequestManager
    private val args by navArgs<SeriesFragmentArgs>()
    private val _binding: FragmentSeriesBinding by viewBinding()
    private val _getSeriesIntentPublisher =
        PublishSubject.create<SeriesIntent.GetSeriesIntent>()
    private val _getSeriesByCategoryIntentPublisher =
        PublishSubject.create<SeriesIntent.GetSeriesByCategoryIntent>()
    private val _getSeriesLikeIntentPublisher =
        PublishSubject.create<SeriesIntent.GetSeriesLikeIntent>()
    private val _getSeriesByCategoryLikeIntentPublisher =
        PublishSubject.create<SeriesIntent.GetSeriesByCategoryLikeIntent>()
    private val _viewModel: SeriesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AdapterSeries2(this, requestManager)
        bind()
        when (args.type) {
            SeriesTypeAction.GET_ALL -> _getSeriesIntentPublisher.onNext(SeriesIntent.GetSeriesIntent)
            SeriesTypeAction.GET_BY -> _getSeriesByCategoryIntentPublisher.onNext(
                SeriesIntent.GetSeriesByCategoryIntent(
                    args.id
                )
            )
        }
    }

    private fun bind() {
        _viewModel.states().subscribe(this::render).disposeOnDestroy()
        _viewModel.processIntents(intents())
    }

    override fun intents(): Observable<SeriesIntent> {
        return Observable.merge(
            _getSeriesIntentPublisher,
            _getSeriesByCategoryIntentPublisher,
            _getSeriesLikeIntentPublisher,
            _getSeriesByCategoryLikeIntentPublisher
        )
    }

    override fun render(state: SeriesViewState) {
        if (state.error != null) {
            Timber.e(state.error)
            return
        }
        Timber.e(state.isLoading.toString())
        adapter.submitList(state.series)
        _binding.apply {
            rvSeries.adapter = adapter
            ivAction.setOnClickListener { onBackPressed() }
            etSearch.addTextChangedListener {
                when (args.type) {
                    SeriesTypeAction.GET_ALL -> _getSeriesLikeIntentPublisher.onNext(
                        SeriesIntent.GetSeriesLikeIntent(
                            etSearch.text.toString()
                        )
                    )
                    SeriesTypeAction.GET_BY -> _getSeriesByCategoryLikeIntentPublisher.onNext(
                        SeriesIntent.GetSeriesByCategoryLikeIntent(
                            etSearch.text.toString(),
                            args.id
                        )
                    )
                }
            }
        }
    }

    override fun onClickSeries(series: Series) {
        findNavController().navigate(actionSeriesToDetail(series))
    }
}