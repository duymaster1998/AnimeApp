package edu.nuce.cinema.ui.library.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.nuce.base.delegates.viewBinding
import edu.nuce.base.view.BaseDialogFragment
import edu.nuce.cinema.R
import edu.nuce.cinema.data.models.Archive
import edu.nuce.cinema.databinding.DialogActionBinding
import edu.nuce.cinema.ui.library.LibraryEvents

class ActionDialog(private val events: LibraryEvents,private val input:Archive) : BaseDialogFragment(), View.OnClickListener {

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
            btnClose.setOnClickListener(this@ActionDialog)
            btnDelete.setOnClickListener {
                events.onDeleteArchive(input)
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