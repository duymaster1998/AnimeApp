package edu.nuce.cinema.ui.report

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import dagger.hilt.android.scopes.FragmentScoped
import edu.nuce.base.mvi.MviViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.subjects.PublishSubject

@FragmentScoped
class ReportCommentViewModel @ViewModelInject constructor(
    private val _reportCommentActionProcessor: ReportCommentActionProcessor
) : ViewModel(), MviViewModel<ReportCommentIntent, ReportCommentViewState> {

    private val disposables = CompositeDisposable()
    private val intentSubject: PublishSubject<ReportCommentIntent> = PublishSubject.create()
    private val statesObservable: Observable<ReportCommentViewState> = compose()

    override fun processIntents(intents: Observable<ReportCommentIntent>) {
        intents.subscribe(intentSubject::onNext).disposeOnCleared()
    }

    override fun states(): Observable<ReportCommentViewState> = statesObservable

    private fun compose(): Observable<ReportCommentViewState> {
        return intentSubject
            .map(this::actionFromIntent)
            .compose(_reportCommentActionProcessor.actionProcessor)
            .scan(ReportCommentViewState.initialState(), reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: ReportCommentIntent): ReportCommentAction {
        return when (intent) {
            is ReportCommentIntent.CheckReportIntent -> ReportCommentAction.CheckReportAction(intent.id)
            is ReportCommentIntent.ReportIntent -> ReportCommentAction.ReportAction(
                intent.reportParams,
                intent.id
            )
        }
    }

    companion object {
        private val reducer =
            BiFunction { previousState: ReportCommentViewState, result: ReportCommentResult ->
                when (result) {
                    is ReportCommentResult.CheckReportResult -> when (result) {
                        is ReportCommentResult.CheckReportResult.Success -> {
                            previousState.copy(
                                isLoading = false,
                                checkReport = result.checkReport
                            )
                        }
                        is ReportCommentResult.CheckReportResult.Failure -> {
                            previousState.copy(
                                isLoading = false,
                                error = result.error
                            )
                        }
                        is ReportCommentResult.CheckReportResult.InFlight -> previousState.copy(
                            isLoading = true
                        )
                    }
                }
            }
    }

    private fun Disposable.disposeOnCleared(): Disposable {
        disposables.add(this)
        return this
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}