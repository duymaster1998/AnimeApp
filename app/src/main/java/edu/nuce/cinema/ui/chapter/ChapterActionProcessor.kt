package edu.nuce.cinema.ui.chapter

import edu.nuce.cinema.data.network.ApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class ChapterActionProcessor @Inject constructor(
    private val apiService: ApiService
) {

    private val getChapterMangaProcessor =
        ObservableTransformer<ChapterAction.GetChapterMangaAction, ChapterResult.GetChapterMangaResult> { action ->
            action.flatMap { ChapterAction ->
                apiService.getMangaBySeason(ChapterAction.id)
                    .map { ChapterResult.GetChapterMangaResult.Success(mangas = it) }
                    .cast(ChapterResult.GetChapterMangaResult::class.java)
                    .onErrorReturn(ChapterResult.GetChapterMangaResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(ChapterResult.GetChapterMangaResult.InFlight)
            }
        }
    private val updateMangaProcessor =
        ObservableTransformer<ChapterAction.UpdateMangaAction, ChapterResult.UpdateMangaResult> { action ->
            action.flatMap {
                apiService.updateViewManga(it.id)
                    .toObservable()
                    .map { ChapterResult.UpdateMangaResult.Success(manga = it) }
                    .cast(ChapterResult.UpdateMangaResult::class.java)
                    .onErrorReturn(ChapterResult.UpdateMangaResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(ChapterResult.UpdateMangaResult.InFlight)
            }
        }
    internal var actionProcessor = ObservableTransformer<ChapterAction, ChapterResult> { actions ->
        actions.publish { shared ->
            Observable.merge(
                shared.ofType(ChapterAction.GetChapterMangaAction::class.java)
                    .compose(getChapterMangaProcessor),
                shared.ofType(ChapterAction.UpdateMangaAction::class.java)
                    .compose(updateMangaProcessor)
            )
        }
    }
}