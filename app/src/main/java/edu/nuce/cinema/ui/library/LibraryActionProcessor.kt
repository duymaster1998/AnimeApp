package edu.nuce.cinema.ui.library

import edu.nuce.cinema.data.network.ApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class LibraryActionProcessor @Inject constructor(
    private val apiService: ApiService
) {

    private val getSubscribeSeriesProcessor =
        ObservableTransformer<LibraryAction.GetSubscibeSeriesAction, LibraryResult.GetSubscribeSeriesResult> { action ->
            action.flatMap {
                apiService.getSubscribeSeries()
                    .map { LibraryResult.GetSubscribeSeriesResult.Success(series = it) }
                    .cast(LibraryResult.GetSubscribeSeriesResult::class.java)
                    .onErrorReturn(LibraryResult.GetSubscribeSeriesResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(LibraryResult.GetSubscribeSeriesResult.InFlight)
            }
        }
    private val getSaveListProcessor =
        ObservableTransformer<LibraryAction.GetSaveListAction, LibraryResult.GetSaveListResult> { action ->
            action.flatMap {
                apiService.getArchiveByUser()
                    .map { LibraryResult.GetSaveListResult.Success(archives = it) }
                    .cast(LibraryResult.GetSaveListResult::class.java)
                    .onErrorReturn(LibraryResult.GetSaveListResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(LibraryResult.GetSaveListResult.InFlight)
            }
        }
    private val addSaveListProcessor =
        ObservableTransformer<LibraryAction.AddSaveListAction, LibraryResult.GetSaveListResult> { action ->
            action.flatMap {
                apiService.addArchive(it.name)
                    .andThen(apiService.getArchiveByUser())
                    .map { LibraryResult.GetSaveListResult.Success(archives = it) }
                    .cast(LibraryResult.GetSaveListResult::class.java)
                    .onErrorReturn(LibraryResult.GetSaveListResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(LibraryResult.GetSaveListResult.InFlight)
            }
        }
    private val removeArchiveProcessor =
        ObservableTransformer<LibraryAction.RemoveArchiveAction, LibraryResult.GetSaveListResult> { action ->
            action.flatMap {
                apiService.removeArchive(it.id)
                    .andThen(apiService.getArchiveByUser())
                    .map { LibraryResult.GetSaveListResult.Success(archives = it) }
                    .cast(LibraryResult.GetSaveListResult::class.java)
                    .onErrorReturn(LibraryResult.GetSaveListResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(LibraryResult.GetSaveListResult.InFlight)
            }
        }
    internal var actionProcessor = ObservableTransformer<LibraryAction, LibraryResult> { actions ->
        actions.publish { shared ->
            Observable.merge(
                shared.ofType(LibraryAction.GetSubscibeSeriesAction::class.java)
                    .compose(getSubscribeSeriesProcessor),
                shared.ofType(LibraryAction.GetSaveListAction::class.java)
                    .compose(getSaveListProcessor),
                shared.ofType(LibraryAction.AddSaveListAction::class.java)
                    .compose(addSaveListProcessor),
                shared.ofType(LibraryAction.RemoveArchiveAction::class.java)
                    .compose(removeArchiveProcessor)
            )
        }
    }
}