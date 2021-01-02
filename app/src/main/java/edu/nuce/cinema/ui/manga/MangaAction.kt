package edu.nuce.cinema.ui.manga

import edu.nuce.base.mvi.MviAction
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.request.CommentParams
import edu.nuce.cinema.data.models.request.HistoryParams

sealed class MangaAction:MviAction {
    data class UpdateMangaAction(val id: Int,val historyParams: HistoryParams) : MangaAction()
    data class GetSeriesAction(val id: Int):MangaAction()
    data class GetArchiveAction(val id: Int):MangaAction()
}