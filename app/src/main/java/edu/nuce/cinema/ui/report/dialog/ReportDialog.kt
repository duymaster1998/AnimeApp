package edu.nuce.cinema.ui.report.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.nuce.base.delegates.viewBinding
import edu.nuce.base.view.BaseDialogFragment
import edu.nuce.cinema.R
import edu.nuce.cinema.data.models.request.ReportParams
import edu.nuce.cinema.databinding.DialogReportBinding
import edu.nuce.cinema.ui.library.LibraryEvents
import edu.nuce.cinema.ui.report.ReportEvents

class ReportDialog(private val events: ReportEvents, private val input: ReportParams) :
    BaseDialogFragment(), View.OnClickListener {

    private val _binding by viewBinding<DialogReportBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding.apply {
            btnClose.setOnClickListener(this@ReportDialog)
            btnDelete.setOnClickListener {
                events.onSubmit(input)
                dialog?.dismiss()
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_close -> dialog?.dismiss()
        }
    }
}