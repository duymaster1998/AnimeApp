package edu.nuce.cinema.ui.genre

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import edu.nuce.base.delegates.viewBinding
import edu.nuce.base.mvi.MviView
import edu.nuce.cinema.R
import edu.nuce.cinema.data.models.Category
import edu.nuce.cinema.databinding.FragmentGenreBinding
import edu.nuce.cinema.ui.base.BaseFragment
import edu.nuce.cinema.ui.common.constants.SeriesTypeAction
import edu.nuce.cinema.ui.genre.adapter.AdapterGenre
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class GenreFragment : BaseFragment(R.layout.fragment_genre),
    MviView<GenreIntent, GenreViewState>, AdapterGenre.GenreOnClick {

    lateinit var adapter: AdapterGenre

    @Inject
    lateinit var requestManager: RequestManager
    private val _binding: FragmentGenreBinding by viewBinding()
    private val _getGenreIntentPublisher =
        PublishSubject.create<GenreIntent.GetGenreIntent>()
    private val _getGenreByLikeIntentPublisher =
        PublishSubject.create<GenreIntent.GetGenreByLikeIntent>()
    private val _viewModel: GenreViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AdapterGenre(this, requestManager)
        bind()
        _getGenreIntentPublisher.onNext(GenreIntent.GetGenreIntent)
    }

    private fun bind() {
        _viewModel.states().subscribe(this::render).disposeOnDestroy()
        _viewModel.processIntents(intents())
    }

    override fun intents(): Observable<GenreIntent> {
        return Observable.merge(
            _getGenreIntentPublisher,
            _getGenreByLikeIntentPublisher
        )
    }

    override fun render(state: GenreViewState) {
        if (state.error != null) {
            Timber.e(state.error)
            return
        }
        Timber.e(state.isLoading.toString())
        adapter.submitList(state.genres)
        _binding.apply {
            rvGenre.adapter = adapter
            etSearch.addTextChangedListener {
                _getGenreByLikeIntentPublisher.onNext(GenreIntent.GetGenreByLikeIntent(etSearch.text.toString()))
            }
        }
    }

    override fun onClickGenre(category: Category) {
        findNavController().navigate(
            GenreFragmentDirections.actionGenreToSeries(
                category.id,
                SeriesTypeAction.GET_BY
            )
        )
    }
}