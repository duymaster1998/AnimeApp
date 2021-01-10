package edu.nuce.cinema.ui.library_detail.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.nuce.base.delegates.viewBinding
import edu.nuce.base.view.BaseDialogFragment
import edu.nuce.cinema.R
import edu.nuce.cinema.data.models.Anime
import edu.nuce.cinema.data.models.Manga
import edu.nuce.cinema.databinding.DialogActionBinding
import edu.nuce.cinema.ui.library_detail.LibraryDetailEvents

class ActionLibraryDialog(
    private val events: LibraryDetailEvents,
    private val anime: Anime?,
    private val manga: Manga?,
) : BaseDialogFragment(), View.OnClickListener {

    private val _binding by viewBinding<DialogActionBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_action, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding.apply {
            btnClose.setOnClickListener(this@ActionLibraryDialog)
            if (anime == null && manga != null) {
                btnDelete.setOnClickListener {
                    events.onDeleteManga(manga)
                    dialog?.dismiss()
                }
            } else if (anime != null && manga == null) {
                btnDelete.setOnClickListener {
                    events.onDeleteAnime(anime)
                    dialog?.dismiss()
                }
            }

        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_close -> dialog?.dismiss()
        }
    }

}