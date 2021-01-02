package edu.nuce.cinema.ui.genre

import edu.nuce.cinema.data.network.ApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class GenreActionProcessor @Inject constructor(
    private val _apiService: ApiService,
) {
    private val getGenreProcessor =
        ObservableTransformer<GenreAction.GetGenreAction, GenreResult.GetGenreResult> { action ->
            action.flatMap {
                _apiService.getCategories()
                    .map { GenreResult.GetGenreResult.Success(genres = it) }
                    .cast(GenreResult.GetGenreResult::class.java)
                    .onErrorReturn(GenreResult.GetGenreResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(GenreResult.GetGenreResult.InFlight)
            }
        }
    private val getGenreByLikeActionProcessor =
        ObservableTransformer<GenreAction.GetGenreByLikeAction,GenreResult.GetGenreResult>{ action->
            action.flatMap {
                _apiService.getCategoriesLike(it.input)
                    .map { GenreResult.GetGenreResult.Success(genres = it) }
                    .cast(GenreResult.GetGenreResult::class.java)
                    .onErrorReturn(GenreResult.GetGenreResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(GenreResult.GetGenreResult.InFlight)
            }
        }
    internal var actionProcessor = ObservableTransformer<GenreAction, GenreResult> { actions ->
        actions.publish { shared ->
            Observable.merge(
                shared.ofType(GenreAction.GetGenreAction::class.java).compose(getGenreProcessor),
                shared.ofType(GenreAction.GetGenreByLikeAction::class.java).compose(getGenreByLikeActionProcessor)
            )
        }
    }
}