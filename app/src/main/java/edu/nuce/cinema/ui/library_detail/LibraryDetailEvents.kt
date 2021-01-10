package edu.nuce.cinema.ui.library_detail

import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.Manga

interface LibraryDetailEvents {
    fun onDeleteAnime(input:Anime)
    fun onDeleteManga(input: Manga)
}