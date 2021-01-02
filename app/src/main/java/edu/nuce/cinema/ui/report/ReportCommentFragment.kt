package edu.nuce.cinema.ui.report

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import edu.nuce.base.delegates.viewBinding
import edu.nuce.base.extensions.onBackPressed
import edu.nuce.base.extensions.toast
import edu.nuce.base.mvi.MviView
import edu.nuce.cinema.R
import edu.nuce.cinema.data.models.request.ReportParams
import edu.nuce.cinema.databinding.FragmentReportBinding
import edu.nuce.cinema.ui.base.BaseFragment
import edu.nuce.cinema.ui.report.dialog.ReportDialog
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber

@AndroidEntryPoint
class ReportCommentFragment : BaseFragment(R.layout.fragment_report),
    MviView<ReportCommentIntent, ReportCommentViewState>, ReportEvents {

    lateinit var reportDialog: ReportDialog
    private val args by navArgs<ReportCommentFragmentArgs>()
    private val _binding: FragmentReportBinding by viewBinding()
    private val _checkReportIntentPublisher =
        PublishSubject.create<ReportCommentIntent.CheckReportIntent>()
    private val _reportPublisher = PublishSubject.create<ReportCommentIntent.ReportIntent>()
    private val _viewModel: ReportCommentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        _checkReportIntentPublisher.onNext(ReportCommentIntent.CheckReportIntent(args.commentId))
    }

    private fun bind() {
        _viewModel.states().subscribe(this::render).disposeOnDestroy()
        _viewModel.processIntents(intents())
    }

    override fun intents(): Observable<ReportCommentIntent> {
        return Observable.merge(
            _checkReportIntentPublisher,
            _reportPublisher
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun render(state: ReportCommentViewState) {
        if (state.error != null) {
            Timber.e(state.error)
            return
        }
        Timber.e(state.isLoading.toString())
        _binding.apply {
            include.apply {
                imgAction.setImageDrawable(resources.getDrawable(R.drawable.ic_previous_black))
                title.text = resources.getString(R.string.report)
                imgAction.setOnClickListener { onBackPressed() }
            }
            btnSubmit.setOnClickListener {
                if (!state.checkReport) {
                    if (etProblem.text.isNotEmpty() && etMoreReport.text.isNotEmpty()) {
                        _reportPublisher.onNext(
                            ReportCommentIntent.ReportIntent(
                                ReportParams(
                                    args.commentId,
                                    etMoreReport.text.toString(),
                                    etProblem.text.toString()
                                ), args.commentId
                            )
                        )
                        toast("Report Success")
                        etMoreReport.text.clear()
                        etProblem.text.clear()
                    }
                } else {
                    reportDialog = ReportDialog(
                        this@ReportCommentFragment, ReportParams(
                            args.commentId,
                            etMoreReport.text.toString(),
                            etProblem.text.toString()
                        )
                    )
                    this@ReportCommentFragment.fragmentManager?.let {
                        reportDialog.show(it, "name")
                    }
                }
            }
        }
    }

    override fun onSubmit(input: ReportParams) {
        _reportPublisher.onNext(
            ReportCommentIntent.ReportIntent(input, args.commentId)
        )
        _binding.etProblem.text.clear()
        _binding.etMoreReport.text.clear()
        toast("Report Success")
    }
}