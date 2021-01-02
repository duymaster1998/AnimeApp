package edu.nuce.cinema.ui.library

import edu.nuce.cinema.data.models.Archive

interface LibraryEvents {
    fun onClickArchive(archive: Archive)
    fun onLongClickArchive(archive: Archive):Boolean
    fun addArchive(name:String)
    fun onDeleteArchive(input:Archive)
}