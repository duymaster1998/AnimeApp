package edu.nuce.cinema.ui.report

import edu.nuce.cinema.data.models.request.ReportParams

interface ReportEvents {
    fun onSubmit(input:ReportParams)
}