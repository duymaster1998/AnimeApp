package edu.nuce.cinema.ui.report

import edu.nuce.cinema.data.network.ApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class ReportCommentActionProcessor @Inject constructor(
    private val _apiService: ApiService,
) {
    private val checkReportProcessor =
        ObservableTransformer<ReportCommentAction.CheckReportAction, ReportCommentResult.CheckReportResult> { action ->
            action.flatMap {
                _apiService.checkReport(it.id)
                    .toObservable()
                    .map { ReportCommentResult.CheckReportResult.Success(checkReport = it) }
                    .cast(ReportCommentResult.CheckReportResult::class.java)
                    .onErrorReturn(ReportCommentResult.CheckReportResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(ReportCommentResult.CheckReportResult.InFlight)
            }
        }
    private val reportCommentActionProcessor =
        ObservableTransformer<ReportCommentAction.ReportAction,ReportCommentResult.CheckReportResult>{ action->
            action.flatMap {
                _apiService.reportComment(it.reportParams)
                    .andThen(_apiService.checkReport(it.id))
                    .toObservable()
                    .map { ReportCommentResult.CheckReportResult.Success(checkReport = it) }
                    .cast(ReportCommentResult.CheckReportResult::class.java)
                    .onErrorReturn(ReportCommentResult.CheckReportResult::Failure)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWithItem(ReportCommentResult.CheckReportResult.InFlight)
            }
        }
    internal var actionProcessor = ObservableTransformer<ReportCommentAction, ReportCommentResult> { actions ->
        actions.publish { shared ->
            Observable.merge(
                shared.ofType(ReportCommentAction.CheckReportAction::class.java).compose(checkReportProcessor),
                shared.ofType(ReportCommentAction.ReportAction::class.java).compose(reportCommentActionProcessor)
            )
        }
    }
}