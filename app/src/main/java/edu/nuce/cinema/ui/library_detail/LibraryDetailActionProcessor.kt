package edu.nuce.cinema.ui.LibraryDetail_detail

import edu.nuce.cinema.data.network.ApiService
import edu.nuce.cinema.ui.library_detail.LibraryDetailAction
import edu.nuce.cinema.ui.library_detail.LibraryDetailResult
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class LibraryDetailActionProcessor @Inject constructor(
    private val apiService: ApiService
) {

    private val getAnimeProcessor =
        ObservableTransformer<LibraryDetailAction.GetAnimeAction, LibraryDetailResult.GetAnimeResult> { action ->
            action.flatMap {
                apiService.getAnimeHistories()
                    .map { LibraryDetailResult.GetAnimeResult.Success(animes = it) }
                    .cast(LibraryDetailResult.GetAnimeResult::class.java)
                    .onErrorReturn(LibraryDetailResult.GetAnimeResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(LibraryDetailResult.GetAnimeResult.InFlight)
            }
        }
    private val getMangaProcessor =
        ObservableTransformer<LibraryDetailAction.GeMangaAction, LibraryDetailResult.GetMangaResult> { action ->
            action.flatMap {
                apiService.getMangaHistories()
                    .map { LibraryDetailResult.GetMangaResult.Success(mangas = it) }
                    .cast(LibraryDetailResult.GetMangaResult::class.java)
                    .onErrorReturn(LibraryDetailResult.GetMangaResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(LibraryDetailResult.GetMangaResult.InFlight)
            }
        }
    private val getAnimeByArchiveProcessor =
        ObservableTransformer<LibraryDetailAction.GetAnimeByArchiveAction, LibraryDetailResult.GetAnimeResult> { action ->
            action.flatMap {
                apiService.getAnimeByArchive(it.id)
                    .map { LibraryDetailResult.GetAnimeResult.Success(animes = it) }
                    .cast(LibraryDetailResult.GetAnimeResult::class.java)
                    .onErrorReturn(LibraryDetailResult.GetAnimeResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(LibraryDetailResult.GetAnimeResult.InFlight)
            }
        }
    private val getMangaByArchiveProcessor =
        ObservableTransformer<LibraryDetailAction.GetMangaByArchiveAction, LibraryDetailResult.GetMangaResult> { action ->
            action.flatMap {
                apiService.getMangaByArchive(it.id)
                    .map { LibraryDetailResult.GetMangaResult.Success(mangas = it) }
                    .cast(LibraryDetailResult.GetMangaResult::class.java)
                    .onErrorReturn(LibraryDetailResult.GetMangaResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(LibraryDetailResult.GetMangaResult.InFlight)
            }
        }
    internal var actionProcessor = ObservableTransformer<LibraryDetailAction, LibraryDetailResult> { actions ->
        actions.publish { shared ->
            Observable.merge(
                shared.ofType(LibraryDetailAction.GetAnimeAction::class.java)
                    .compose(getAnimeProcessor),
                shared.ofType(LibraryDetailAction.GeMangaAction::class.java)
                    .compose(getMangaProcessor),
                shared.ofType(LibraryDetailAction.GetAnimeByArchiveAction::class.java)
                    .compose(getAnimeByArchiveProcessor),
                shared.ofType(LibraryDetailAction.GetMangaByArchiveAction::class.java)
                    .compose(getMangaByArchiveProcessor),
            )
        }
    }
}