package edu.nuce.cinema.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import edu.nuce.base.auth.RxOAuthManager
import edu.nuce.base.delegates.put
import edu.nuce.base.delegates.viewBinding
import edu.nuce.base.extensions.instance
import edu.nuce.base.extensions.onBackPressed
import edu.nuce.base.extensions.toast
import edu.nuce.base.mvi.MviView
import edu.nuce.cinema.R
import edu.nuce.cinema.data.models.Series
import edu.nuce.cinema.databinding.FragmentDetailBinding
import edu.nuce.cinema.ui.base.BaseFragment
import edu.nuce.cinema.ui.detail.DetailFragmentDirections.Companion.actionDetailToLogin
import edu.nuce.cinema.ui.detail.adapter.AdapterCategorySeries
import edu.nuce.cinema.ui.detail.adapter.PageAdapter
import edu.nuce.cinema.ui.detail.fragments.description.DescriptionFragment
import edu.nuce.cinema.ui.detail.fragments.season_anime.SeasonAnimeFragment
import edu.nuce.cinema.ui.detail.fragments.season_manga.SeasonMangaFragment
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : BaseFragment(R.layout.fragment_detail), DetailEvents,
    MviView<DetailIntent, DetailViewState> {

    private val _binding: FragmentDetailBinding by viewBinding()
    private lateinit var viewpagerAdapter: PageAdapter
    private val args by navArgs<DetailFragmentArgs>()
    private val series: Series by lazy(LazyThreadSafetyMode.NONE) { args.series }
    private lateinit var adapterCategorySeries: AdapterCategorySeries

    private val _getCategoryBySeriesItentPublisher =
        PublishSubject.create<DetailIntent.GetCategoryBySeriesIntent>()
    private val _getRateSeriesItentPublisher =
        PublishSubject.create<DetailIntent.GetRateSeriesIntent>()
    private val _isFollowIntenPublisher =
        PublishSubject.create<DetailIntent.IsFollowIntent>()
    private val _followItentPublisher =
        PublishSubject.create<DetailIntent.FollowIntent>()
    private val _unFollowItentPublisher =
        PublishSubject.create<DetailIntent.UnFollowIntent>()
    private val _viewModel: DetailViewModel by viewModels()

    @Inject
    lateinit var requestManager: RequestManager

    @Inject
    lateinit var rxOAuthManager: RxOAuthManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        _getCategoryBySeriesItentPublisher.onNext(DetailIntent.GetCategoryBySeriesIntent(args.series.id))
        _getRateSeriesItentPublisher.onNext(DetailIntent.GetRateSeriesIntent(args.series.id))
        if (rxOAuthManager.isExistsCredentials) {
            _isFollowIntenPublisher.onNext(DetailIntent.IsFollowIntent(args.series.id))
        }

        val tabLayout = _binding.tabLayout
        //list child fragments
        val list = listOf(
            instance<DescriptionFragment> {
                put(DescriptionFragment::class.java.simpleName, series)
            },
            instance<SeasonAnimeFragment> {
                put(SeasonAnimeFragment::class.java.simpleName, series)
            },
            instance<SeasonMangaFragment> {
                put(SeasonMangaFragment::class.java.simpleName, series)
            }
        )

        viewpagerAdapter = PageAdapter(requireActivity(), list)
        _binding.viewPager2.adapter = viewpagerAdapter
        TabLayoutMediator(tabLayout, _binding.viewPager2) { tab, position ->
            tab.text = when (position) {
                0 -> "Description"
                1 -> "Anime"
                2 -> "Manga"
                else -> "Description"
            }

        }.attach()

    }

    private fun bind() {
        _viewModel.states().subscribe(this::render).disposeOnDestroy()
        _viewModel.processIntents(intents())
    }

    override fun intents(): Observable<DetailIntent> {
        return Observable.merge(
            _getCategoryBySeriesItentPublisher,
            _getRateSeriesItentPublisher,
            _followItentPublisher,
            _isFollowIntenPublisher
        ).mergeWith(
            _unFollowItentPublisher
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun render(state: DetailViewState) {
        if (state.error != null) {
            Timber.e(state.error)
            return
        }
        Timber.e(state.isLoading.toString())
        if (state.isFollow)
            _binding.btnSubscribe.setImageDrawable(resources.getDrawable(R.drawable.ic_heart_color))
        else
            _binding.btnSubscribe.setImageDrawable(resources.getDrawable(R.drawable.ic_heart))
        adapterCategorySeries = AdapterCategorySeries(requestManager)
        adapterCategorySeries.submitList(state.categories)
        _binding.run {
            rvCategory.apply {
                setHasFixedSize(true)
                adapter = adapterCategorySeries
            }
            requestManager.load(args.series.backdropPath).into(image)
            tvName.text = args.series.name
            include.imgAction.setOnClickListener { onBackPressed() }
            btnSubscribe.setOnClickListener {
                onSubscribe(state.isFollow)
            }
        }
    }

    override fun onSubscribe(state: Boolean) {
        if (rxOAuthManager.isExistsCredentials) {
            if (state == false) {
                _followItentPublisher.onNext(DetailIntent.FollowIntent(args.series.id))
                toast(resources.getString(R.string.subcribe))
            } else if(state == true) {
                _unFollowItentPublisher.onNext(DetailIntent.UnFollowIntent(args.series.id))
                toast(resources.getString(R.string.unsubscribe))
            }
        } else
            findNavController().navigate(actionDetailToLogin())
    }
}