package edu.nuce.cinema.ui.manga

import edu.nuce.cinema.data.network.ApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MangaActionProcessor @Inject constructor(
    private val apiService: ApiService
) {
    private val updateMangaProcessor =
        ObservableTransformer<MangaAction.UpdateMangaAction, MangaResult.UpdateMangaResult> { action ->
            action.flatMap {
                apiService.historySeries(it.historyParams)
                    .andThen(apiService.updateViewManga(it.id))
                    .toObservable()
                    .map { MangaResult.UpdateMangaResult.Success(Manga = it) }
                    .cast(MangaResult.UpdateMangaResult::class.java)
                    .onErrorReturn(MangaResult.UpdateMangaResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(MangaResult.UpdateMangaResult.InFlight)
            }
        }
    private val getSeriesProcessor =
        ObservableTransformer<MangaAction.GetSeriesAction, MangaResult.GetSeriesResult> { action ->
            action.flatMap {
                apiService.getSeriesByManga(it.id)
                    .toObservable()
                    .map { MangaResult.GetSeriesResult.Success(series = it) }
                    .cast(MangaResult.GetSeriesResult::class.java)
                    .onErrorReturn(MangaResult.GetSeriesResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(MangaResult.GetSeriesResult.InFlight)
            }
        }
    private val getArchiveProcessor =
        ObservableTransformer<MangaAction.GetArchiveAction, MangaResult.GetArchiveResult> { action ->
            action.flatMap {
                apiService.getArchiveOfManga(it.id)
                    .map { MangaResult.GetArchiveResult.Success(archives = it) }
                    .cast(MangaResult.GetArchiveResult::class.java)
                    .onErrorReturn(MangaResult.GetArchiveResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(MangaResult.GetArchiveResult.InFlight)
            }
        }
    internal var actionProcessor = ObservableTransformer<MangaAction, MangaResult> { actions ->
        actions.publish { shared ->
            Observable.merge(
                shared.ofType(MangaAction.UpdateMangaAction::class.java)
                    .compose(updateMangaProcessor),
                shared.ofType(MangaAction.GetSeriesAction::class.java)
                    .compose(getSeriesProcessor),
            ).mergeWith(
                shared.ofType(MangaAction.GetArchiveAction::class.java)
                    .compose(getArchiveProcessor)
            )
        }
    }
}