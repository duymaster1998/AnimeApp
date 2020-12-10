package edu.nuce.cinema.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import edu.nuce.base.delegates.viewBinding
import edu.nuce.base.extensions.toast
import edu.nuce.base.mvi.MviView
import edu.nuce.cinema.R
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.Category
import edu.nuce.cinema.data.models.Series
import edu.nuce.cinema.databinding.FragmentHomeBinding
import edu.nuce.cinema.ui.base.BaseFragment
import edu.nuce.cinema.ui.common.adapter.AdapterSeries
import edu.nuce.cinema.ui.home.adapter.AdapterCategory
import edu.nuce.cinema.ui.home.adapter.AdapterAnime
import edu.nuce.cinema.ui.home.adapter.AdapterAnimeN
import edu.nuce.cinema.ui.home.adapter.SliderAdapter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home), MviView<HomeIntent, HomeViewState>,
    AdapterSeries.SeriesOnClick, AdapterCategory.OnClickCategory,AdapterAnime.OnClickAnime,AdapterAnimeN.OnClickAnime{

    private lateinit var adapterTopManga: AdapterSeries
    private lateinit var adapterRecommend: AdapterSeries
    private lateinit var adaperTopAnime: AdapterAnime
    private lateinit var adapterCategory: AdapterCategory
    private lateinit var adapterAnimeN: AdapterAnimeN

    @Inject
    lateinit var requestManager: RequestManager

    private val _binding: FragmentHomeBinding by viewBinding()
    private val _getAllSeriesIntentPublisher =
        PublishSubject.create<HomeIntent.GetAllSeriesIntent>()
    private val _getTopAnimeIntentPublisher =
        PublishSubject.create<HomeIntent.GetTopAnimeIntent>()
    private val _getCategoryItentPublisher =
        PublishSubject.create<HomeIntent.GetTopCategoryIntent>()
    private val _getNewAnimeItentPublisher =
        PublishSubject.create<HomeIntent.GetNewAnimeIntent>()
    private val _viewModel: HomeViewModel by viewModels()

//    @Inject lateinit var userManager: UserManager
//    private lateinit var userDataRepository: UserDataRepository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //        if (userManager.isUserLoggedIn()) {
//            userDataRepository = EntryPoints.get(
//                userManager.userComponent!!, UserSessionComponentEntryPoint::class.java
//            ).userDataRepository()
//            userDataRepository
//                .getAuthUser()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                    { userDataRepository.cacheUser(it) },
//                    { error -> Timber.e(error) }
//                ).disposeOnDestroy()
        bind()
        _getAllSeriesIntentPublisher.onNext(HomeIntent.GetAllSeriesIntent)
        _getTopAnimeIntentPublisher.onNext(HomeIntent.GetTopAnimeIntent)
        _getCategoryItentPublisher.onNext(HomeIntent.GetTopCategoryIntent)
        _getNewAnimeItentPublisher.onNext(HomeIntent.GetNewAnimeIntent)

    }

    override fun intents(): Observable<HomeIntent> {
        return Observable.merge(
            _getAllSeriesIntentPublisher,
            _getTopAnimeIntentPublisher,
            _getCategoryItentPublisher,
        ).mergeWith(
            _getNewAnimeItentPublisher,
            )
    }

    override fun render(state: HomeViewState) {
        if (state.error != null) {
            Timber.e(state.error)
            return
        }
        Timber.e(state.isLoading.toString())
        setUpAdapter()
        adapterTopManga.submitList(state.allSeries)
        adapterRecommend.submitList(state.allSeries)
        adaperTopAnime.submitList(state.topAnime)
        adapterCategory.submitList(state.categories)
        adapterAnimeN.submitList(state.animeN)
        _binding.run {
            viewPager2.apply {
                adapter = SliderAdapter(series = state.allSeries, requestManager = requestManager)
            }
            indicator.setViewPager(_binding.viewPager2)
            rvTopManga.apply {
                adapter = adapterTopManga
            }
            rvNewAnime.apply {
                adapter = adapterAnimeN
            }
            rvRecommend.apply {
                adapter = adapterRecommend
            }
            rvTopAnime.apply {
                adapter = adaperTopAnime
            }
            rvCategory.apply {
                adapter = adapterCategory
            }
        }
    }

    private fun bind() {
        _viewModel.states().subscribe(this::render).disposeOnDestroy()
        _viewModel.processIntents(intents())
    }

    private fun setUpAdapter() {
        adapterTopManga = AdapterSeries(
            this,
            requestManager,
            resources.getDimension(R.dimen._130sdp).toInt(),
            resources.getDimension(R.dimen._80sdp).toInt()
        )
        adapterRecommend = AdapterSeries(
            this,
            requestManager,
            resources.getDimension(R.dimen._130sdp).toInt(),
            resources.getDimension(R.dimen._120sdp).toInt()
        )
        adaperTopAnime = AdapterAnime(
            this,
            requestManager,
        )
        adapterCategory = AdapterCategory(this, requestManager)

        adapterAnimeN = AdapterAnimeN(this,requestManager)
    }

    override fun onClickSeries(view: View, series: Series) {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToMangaFragment(series.id,isSeries = true))
    }

    override fun onClikCategory(view: View, category: Category) {
        toast(category.name)
    }

    override fun onClickAnime(view: View, series: Series) {
        findNavController().navigate(HomeFragmentDirections.actionHomeToAnimeFragment(series.id,isSeries = true))
    }

    override fun onClickAnimeN(view: View, anime: Anime) {
        findNavController().navigate(HomeFragmentDirections.actionHomeToAnimeFragment(anime.id,isSeries = false))
    }
}