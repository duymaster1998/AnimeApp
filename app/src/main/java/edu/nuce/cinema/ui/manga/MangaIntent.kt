package edu.nuce.cinema.ui.manga

import edu.nuce.base.mvi.MviIntent
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.request.CommentParams
import edu.nuce.cinema.data.models.request.HistoryParams
import edu.nuce.cinema.ui.episode.EpisodeIntent

sealed class MangaIntent : MviIntent {
    data class UpdateMangaIntent(val id: Int,val historyParams: HistoryParams): MangaIntent()
    data class GetSeriesIntent(val id:Int):MangaIntent()
    data class GetArchiveIntent(val id:Int):MangaIntent()
}
