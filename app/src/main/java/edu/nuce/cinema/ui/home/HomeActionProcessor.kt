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

    private val getNewSeriesActionProcessor =
        ObservableTransformer<HomeAction.GetNewSeriesAction, HomeResult.GetNewSeriesResult> { action ->
            action.flatMap {
                apiService.getNewSeries()
                    .map { HomeResult.GetNewSeriesResult.Success(newSeries = it) }
                    .cast(HomeResult.GetNewSeriesResult::class.java)
                    .onErrorReturn(HomeResult.GetNewSeriesResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(HomeResult.GetNewSeriesResult.InFlight)
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
    private val getTopMangaActionProcessor =
        ObservableTransformer<HomeAction.GetTopMangaAction, HomeResult.GetTopMangaResult> { action ->
            action.flatMap {
                apiService.getTopManga()
                    .map { HomeResult.GetTopMangaResult.Success(topManga = it) }
                    .cast(HomeResult.GetTopMangaResult::class.java)
                    .onErrorReturn(HomeResult.GetTopMangaResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(HomeResult.GetTopMangaResult.InFlight)
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
    private val getNewMangaActionProcessor =
        ObservableTransformer<HomeAction.GetNewMangaAction, HomeResult.GetNewMangaResult> { action ->
            action.flatMap {
                apiService.getNewManga()
                    .map { HomeResult.GetNewMangaResult.Success(mangas = it) }
                    .cast(HomeResult.GetNewMangaResult::class.java)
                    .onErrorReturn(HomeResult.GetNewMangaResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(HomeResult.GetNewMangaResult.InFlight)
            }
        }
    internal var actionProcessor = ObservableTransformer<HomeAction, HomeResult> { actions ->
        actions.publish { shared ->
            Observable.merge(
                shared.ofType(HomeAction.GetNewSeriesAction::class.java)
                    .compose(getNewSeriesActionProcessor),
                shared.ofType(HomeAction.GetTopAnimeAction::class.java)
                    .compose(getTopAnimeActionProcessor),
                shared.ofType(HomeAction.GetTopCategoryAction::class.java)
                    .compose(getTopCategoryActionProcessor),
                shared.ofType(HomeAction.GetNewAnimeAction::class.java)
                    .compose(getNewAnimeActionProcessor)
            ).mergeWith(
                shared.ofType(HomeAction.GetNewMangaAction::class.java)
                    .compose(getNewMangaActionProcessor),
            ).mergeWith(
                shared.ofType(HomeAction.GetTopMangaAction::class.java)
                    .compose(getTopMangaActionProcessor),
            )
        }
    }
}