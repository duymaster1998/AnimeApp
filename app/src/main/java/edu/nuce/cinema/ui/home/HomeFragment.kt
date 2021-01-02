package edu.nuce.cinema.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import edu.nuce.base.auth.RxOAuthManager
import edu.nuce.base.delegates.viewBinding
import edu.nuce.base.extensions.toast
import edu.nuce.base.mvi.MviView
import edu.nuce.cinema.R
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.Category
import edu.nuce.cinema.data.models.Manga
import edu.nuce.cinema.data.models.Series
import edu.nuce.cinema.databinding.FragmentHomeBinding
import edu.nuce.cinema.ui.base.BaseFragment
import edu.nuce.cinema.ui.common.adapter.AdapterSeries
import edu.nuce.cinema.ui.common.constants.SeriesTypeAction
import edu.nuce.cinema.ui.home.HomeFragmentDirections.Companion.actionHomeFragmentToMangaFragment
import edu.nuce.cinema.ui.home.HomeFragmentDirections.Companion.actionHomeToAnimeFragment
import edu.nuce.cinema.ui.home.HomeFragmentDirections.Companion.actionHomeToManga
import edu.nuce.cinema.ui.home.HomeFragmentDirections.Companion.actionHomeToSeries
import edu.nuce.cinema.ui.home.adapter.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home), MviView<HomeIntent, HomeViewState>,
    AdapterSeries.SeriesOnClick, AdapterCategory.OnClickCategory,AdapterAnimeN.OnClickAnime,AdapterMangaN.OnClickManga{

    private lateinit var adapterTopManga: AdapterSeries
    private lateinit var adapterRecommend: AdapterSeries
    private lateinit var adaperTopAnime: AdapterSeries
    private lateinit var adapterCategory: AdapterCategory
    private lateinit var adapterAnimeN: AdapterAnimeN
    private lateinit var adapterMangaN: AdapterMangaN

    @Inject
    lateinit var requestManager: RequestManager
    @Inject
    lateinit var oAuthManager: RxOAuthManager

    private val _binding: FragmentHomeBinding by viewBinding()
    private val _getNewSeriesIntentPublisher =
        PublishSubject.create<HomeIntent.GetNewSeriesIntent>()
    private val _getTopAnimeIntentPublisher =
        PublishSubject.create<HomeIntent.GetTopAnimeIntent>()
    private val _getTopMangaIntentPublisher =
        PublishSubject.create<HomeIntent.GetTopMangaIntent>()
    private val _getCategoryItentPublisher =
        PublishSubject.create<HomeIntent.GetTopCategoryIntent>()
    private val _getNewAnimeItentPublisher =
        PublishSubject.create<HomeIntent.GetNewAnimeIntent>()
    private val _getNewMangaItentPublisher =
        PublishSubject.create<HomeIntent.GetNewMangaIntent>()
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
        _getNewSeriesIntentPublisher.onNext(HomeIntent.GetNewSeriesIntent)
        _getTopAnimeIntentPublisher.onNext(HomeIntent.GetTopAnimeIntent)
        _getTopMangaIntentPublisher.onNext(HomeIntent.GetTopMangaIntent)
        _getCategoryItentPublisher.onNext(HomeIntent.GetTopCategoryIntent)
        _getNewAnimeItentPublisher.onNext(HomeIntent.GetNewAnimeIntent)
        _getNewMangaItentPublisher.onNext(HomeIntent.GetNewMangaIntent)

    }

    override fun intents(): Observable<HomeIntent> {
        return Observable.merge(
            _getNewSeriesIntentPublisher,
            _getTopAnimeIntentPublisher,
//            _getSeriesIntentPublisher,
            _getCategoryItentPublisher,
            _getNewAnimeItentPublisher
        ).mergeWith(
            _getNewMangaItentPublisher
        ).mergeWith(
            _getTopMangaIntentPublisher
        )
    }

    override fun render(state: HomeViewState) {
        if (state.error != null) {
            Timber.e(state.error)
            return
        }
        Timber.e(state.isLoading.toString())
        setUpAdapter()
        adapterTopManga.submitList(state.topManga)
        adapterRecommend.submitList(state.newSeries)
        adaperTopAnime.submitList(state.topAnime)
        adapterCategory.submitList(state.categories)
        adapterAnimeN.submitList(state.animeN)
        adapterMangaN.submitList(state.mangaN)
        _binding.run {
            viewPager2.adapter = SliderAdapter(series = state.newSeries, requestManager = requestManager)
            indicator.setViewPager(_binding.viewPager2)
            rvTopManga.adapter = adapterTopManga
            rvNewAnime.adapter = adapterAnimeN
            rvRecommend.adapter = adapterRecommend
            rvTopAnime.adapter = adaperTopAnime
            rvCategory.adapter = adapterCategory
            rvNewManga.adapter = adapterMangaN
            btnSeriseAll.setOnClickListener { findNavController().navigate(actionHomeToSeries(0,SeriesTypeAction.GET_ALL)) }
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
        adaperTopAnime = AdapterSeries(
            this,
            requestManager,
            resources.getDimension(R.dimen._80sdp).toInt(),
            resources.getDimension(R.dimen._110sdp).toInt()
        )
        adapterCategory = AdapterCategory(this, requestManager)
        adapterAnimeN = AdapterAnimeN(this,requestManager)
        adapterMangaN = AdapterMangaN(this,requestManager)

    }

    override fun onClickSeries(view: View, series: Series) {
        findNavController().navigate(actionHomeFragmentToMangaFragment(series))
    }

    override fun onClikCategory(view: View, category: Category) {
        findNavController().navigate(actionHomeToSeries(category.id,SeriesTypeAction.GET_BY))
    }

    override fun onClickAnimeN(view: View, anime: Anime) {
        findNavController().navigate(actionHomeToAnimeFragment(anime))
    }

    override fun onClickMangaN(view: View, manga: Manga) {
        findNavController().navigate(actionHomeToManga(manga))
    }
}