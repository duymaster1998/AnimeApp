package edu.nuce.cinema.ui.anime.dialog

import edu.nuce.cinema.data.network.ApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class SaveActionProcessor @Inject constructor(
    private val apiService: ApiService
) {

    private val getSaveListProcessor =
        ObservableTransformer<SaveAction.GetSaveListAction, SaveResult.GetSaveListResult> { action ->
            action.flatMap {
                apiService.getArchiveByUser()
                    .map { SaveResult.GetSaveListResult.Success(archives = it) }
                    .cast(SaveResult.GetSaveListResult::class.java)
                    .onErrorReturn(SaveResult.GetSaveListResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(SaveResult.GetSaveListResult.InFlight)
            }
        }
    private val addSaveListProcessor =
        ObservableTransformer<SaveAction.AddSaveListAction, SaveResult.GetSaveListResult> { action ->
            action.flatMap {
                apiService.addArchive(it.name)
                    .andThen(apiService.getArchiveByUser())
                    .map { SaveResult.GetSaveListResult.Success(archives = it) }
                    .cast(SaveResult.GetSaveListResult::class.java)
                    .onErrorReturn(SaveResult.GetSaveListResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(SaveResult.GetSaveListResult.InFlight)
            }
        }
    private val StorageActionProcessor =
        ObservableTransformer<SaveAction.StorageAction, SaveResult.StorageResult> { action ->
            action.flatMap {
                apiService.addItemIntoStorage(it.storageParams)
                    .toObservable()
                    .map { SaveResult.StorageResult.Success(message = it) }
                    .cast(SaveResult.StorageResult::class.java)
                    .onErrorReturn(SaveResult.StorageResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(SaveResult.StorageResult.InFlight)
            }
        }
    internal var actionProcessor = ObservableTransformer<SaveAction, SaveResult> { actions ->
        actions.publish { shared ->
            Observable.merge(
                shared.ofType(SaveAction.GetSaveListAction::class.java)
                    .compose(getSaveListProcessor),
                shared.ofType(SaveAction.AddSaveListAction::class.java)
                    .compose(addSaveListProcessor),
                shared.ofType(SaveAction.StorageAction::class.java)
                    .compose(StorageActionProcessor)
            )
        }
    }
}