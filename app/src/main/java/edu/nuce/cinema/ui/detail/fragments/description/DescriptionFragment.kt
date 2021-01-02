package edu.nuce.cinema.ui.detail.fragments.description

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import edu.nuce.base.auth.RxOAuthManager
import edu.nuce.base.delegates.arg
import edu.nuce.base.delegates.viewBinding
import edu.nuce.base.extensions.toast
import edu.nuce.base.mvi.MviView
import edu.nuce.cinema.R
import edu.nuce.cinema.data.models.Rating
import edu.nuce.cinema.data.models.Series
import edu.nuce.cinema.databinding.FragmentDescriptionBinding
import edu.nuce.cinema.ui.Description.fragments.description.DescriptionViewModel
import edu.nuce.cinema.ui.base.BaseFragment
import edu.nuce.cinema.ui.common.adapter.AdapterSeries
import edu.nuce.cinema.ui.detail.DetailFragmentDirections.Companion.actionDetailSelf
import edu.nuce.cinema.ui.detail.DetailFragmentDirections.Companion.actionDetailToLogin
import edu.nuce.cinema.ui.dialog.RatingDialog
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DescriptionFragment : BaseFragment(R.layout.fragment_description), DescriptionEvents,
    MviView<DescriptionIntent, DescriptionViewState>, AdapterSeries.SeriesOnClick {

    private lateinit var recommendAdapter: AdapterSeries

    @Inject
    lateinit var requestManager: RequestManager

    @Inject
    lateinit var rxOAuthManager: RxOAuthManager

    lateinit var ratingDialog: RatingDialog
    private val binding by viewBinding<FragmentDescriptionBinding>()
    private val series by arg<Series?>(DescriptionFragment::class.java.simpleName)
    private val _getRecommendItentPublisher =
        PublishSubject.create<DescriptionIntent.GetRecommendIntent>()
    private val _getRateSeriesItentPublisher =
        PublishSubject.create<DescriptionIntent.GetRateSeriesIntent>()
    private val _getAuthorItentPublisher =
        PublishSubject.create<DescriptionIntent.GetAuthorIntent>()
    private val _getRateMeItentPublisher =
        PublishSubject.create<DescriptionIntent.GetRateMeIntent>()
    private val _ratingPublisher =
        PublishSubject.create<DescriptionIntent.RatingIntent>()
    private val _viewModel: DescriptionViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        series?.let {
            Timber.e(it.toString())
            _getRecommendItentPublisher.onNext(DescriptionIntent.GetRecommendIntent(it.id))
            _getRateSeriesItentPublisher.onNext(DescriptionIntent.GetRateSeriesIntent(it.id))
            _getAuthorItentPublisher.onNext(DescriptionIntent.GetAuthorIntent(it.id))
            _getRateMeItentPublisher.onNext(DescriptionIntent.GetRateMeIntent(it.id))
        }

    }

    private fun bind() {
        _viewModel.states().subscribe(this::render).disposeOnDestroy()
        _viewModel.processIntents(intents())
    }

    override fun intents(): Observable<DescriptionIntent> {
        return Observable.merge(
            _getRecommendItentPublisher,
            _getRateSeriesItentPublisher,
            _getAuthorItentPublisher,
            _getRateMeItentPublisher
        ).mergeWith(
            _ratingPublisher
        )
    }

    override fun render(state: DescriptionViewState) {
        recommendAdapter = AdapterSeries(
            this,
            requestManager = requestManager,
            resources.getDimension(R.dimen._80sdp).toInt(),
            resources.getDimension(R.dimen._110sdp).toInt()
        )
        var author: String = "-"
        recommendAdapter.submitList(state.series)
        binding.apply {
            ratingBar.rating = state.rating
            rating.text = state.rating.toString()
            content.text = series?.sumary
            rvRecommend.adapter = recommendAdapter
            state.authors.forEach { action -> author += action.name + " - " }
            ratingDialog = RatingDialog(state.rateMe.toFloat(), this@DescriptionFragment)
            tvAuthor.text = author
            viewRating.setOnClickListener {
                if (rxOAuthManager.isExistsCredentials)
                    this@DescriptionFragment.fragmentManager?.let { it ->
                        ratingDialog.show(
                            it,
                            "name"
                        )
                    }
                else
                    findNavController().navigate(actionDetailToLogin())
            }
        }
    }

    override fun onClickSeries(view: View, series: Series) {
            findNavController().navigate(actionDetailSelf(series))
    }

    override fun onRating(rating: Float) {
        _ratingPublisher.onNext(
            DescriptionIntent.RatingIntent(
                Rating(
                    rating = rating.toInt(),
                    series?.id!!.toInt()
                ),series?.id!!.toInt()
            )
        )
        toast("Thank you for rating")
    }

}

//sealed class Result<out R> {
//    data class Success<out T>(val data: T) : Result<T>()
//    data class Error(val error: Throwable) : Result<Nothing>()
//}