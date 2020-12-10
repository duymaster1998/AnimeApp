package edu.nuce.cinema.ui.home

import edu.nuce.cinema.data.network.ApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class HomeActionProcessor @Inject constructor(
    private val apiService: ApiService
) {

    private val getAllSeriesActionProcessor =
        ObservableTransformer<HomeAction.GetAllSeriesAction, HomeResult.GetAllSeriesResult> { action ->
            action.flatMap {
                apiService.getSeries()
                    .map { HomeResult.GetAllSeriesResult.Success(allSeries = it) }
                    .cast(HomeResult.GetAllSeriesResult::class.java)
                    .onErrorReturn(HomeResult.GetAllSeriesResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(HomeResult.GetAllSeriesResult.InFlight)
            }
        }
    private val getTopAnimeActionProcessor =
        ObservableTransformer<HomeAction.GetTopAnimeAction, HomeResult.GetTopAnimeResult> { action ->
            action.flatMap {
                apiService.getTopAnime()
                    .map { HomeResult.GetTopAnimeResult.Success(topAnime = it) }
                    .cast(HomeResult.GetTopAnimeResult::class.java)
                    .onErrorReturn(HomeResult.GetTopAnimeResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(HomeResult.GetTopAnimeResult.InFlight)
            }
        }
    private val getTopCategoryActionProcessor =
        ObservableTransformer<HomeAction.GetTopCategoryAction, HomeResult.GetTopCategoryResult> { action ->
            action.flatMap {
                apiService.getTopCategories()
                    .map { HomeResult.GetTopCategoryResult.Success(categories = it) }
                    .cast(HomeResult.GetTopCategoryResult::class.java)
                    .onErrorReturn(HomeResult.GetTopCategoryResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(HomeResult.GetTopCategoryResult.InFlight)
            }
        }
    private val getNewAnimeActionProcessor =
        ObservableTransformer<HomeAction.GetNewAnimeAction, HomeResult.GetNewAnimeResult> { action ->
            action.flatMap {
                apiService.getNewAnime()
                    .map { HomeResult.GetNewAnimeResult.Success(animes = it) }
                    .cast(HomeResult.GetNewAnimeResult::class.java)
                    .onErrorReturn(HomeResult.GetNewAnimeResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(HomeResult.GetNewAnimeResult.InFlight)
            }
        }
    internal var actionProcessor = ObservableTransformer<HomeAction, HomeResult> { actions ->
        actions.publish { shared ->
            Observable.merge(
                shared.ofType(HomeAction.GetAllSeriesAction::class.java)
                    .compose(getAllSeriesActionProcessor),
                shared.ofType(HomeAction.GetTopAnimeAction::class.java)
                    .compose(getTopAnimeActionProcessor),
                shared.ofType(HomeAction.GetTopCategoryAction::class.java)
                    .compose(getTopCategoryActionProcessor),

            ).mergeWith(
                shared.ofType(HomeAction.GetNewAnimeAction::class.java)
                    .compose(getNewAnimeActionProcessor)
            )
        }
    }
}