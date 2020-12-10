package edu.nuce.cinema.ui.manga

import android.os.Bundle
import android.view.Display
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import edu.nuce.base.delegates.viewBinding
import edu.nuce.base.extensions.onBackPressed
import edu.nuce.base.mvi.MviView
import edu.nuce.cinema.R
import edu.nuce.cinema.databinding.FragmentMangaBinding
import edu.nuce.cinema.ui.base.BaseFragment
import edu.nuce.cinema.ui.manga.adapter.AdapterCategorySeries
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_description.*
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MangaFragment : BaseFragment(R.layout.fragment_manga), MviView<MangaIntent, MangaViewState> {
    private val _binding: FragmentMangaBinding by viewBinding()
    private val args by navArgs<MangaFragmentArgs>()
    private lateinit var adapterCategorySeries: AdapterCategorySeries

    private val _getCategoryBySeriesItentPublisher =
        PublishSubject.create<MangaIntent.GetCategoryBySeriesIntent>()
    private val _getRateSeriesItentPublisher =
        PublishSubject.create<MangaIntent.GetRateSeriesIntent>()
    private val _viewModel: MangaViewModel by viewModels()

    @Inject
    lateinit var requestManager: RequestManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        _getCategoryBySeriesItentPublisher.onNext(MangaIntent.GetCategoryBySeriesIntent(id))
        _getRateSeriesItentPublisher.onNext(MangaIntent.GetRateSeriesIntent(id))
    }

    private fun bind() {
        _viewModel.states().subscribe(this::render).disposeOnDestroy()
        _viewModel.processIntents(intents())
    }

    override fun intents(): Observable<MangaIntent> {
        return Observable.merge(
            _getCategoryBySeriesItentPublisher,
            _getRateSeriesItentPublisher
        )
    }

    override fun render(state: MangaViewState) {
        if (state.error != null) {
            Timber.e(state.error)
            return
        }
        Timber.e(state.isLoading.toString())
        adapterCategorySeries = AdapterCategorySeries(requestManager)
        adapterCategorySeries.submitList(state.categories)
        _binding.run {
            description.apply {
                rvCategory.adapter = adapterCategorySeries
//                tvName.text  = args.series.name
                layout.layoutParams.width = requireActivity().windowManager.defaultDisplay.width
//                content.text = args.series.sumary
            }
            chapters.apply {
                layout.layoutParams.width = requireActivity().windowManager.defaultDisplay.width
            }
//            requestManager.load(args.series.backdropPath).into(image)
            include.imgAction.setOnClickListener { onBackPressed() }
        }
    }
}