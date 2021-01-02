package edu.nuce.cinema.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.nuce.base.delegates.viewBinding
import edu.nuce.base.view.BaseDialogFragment
import edu.nuce.cinema.R
import edu.nuce.cinema.databinding.DialogArchiveBinding
import edu.nuce.cinema.ui.library.LibraryEvents

class ArchiveDialog(
    private val events: LibraryEvents
) : BaseDialogFragment(), View.OnClickListener {

    private val _binding by viewBinding<DialogArchiveBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_archive, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding.apply {
            btnClose.setOnClickListener(this@ArchiveDialog)
            btnSubmit.setOnClickListener {
                events.addArchive(etArchive.text.toString())
                dialog?.dismiss()
            }
        }
    }

//    companion object {
//        fun from(error: String): RatingDialog {
//            return RatingDialog(error)
//        }
//    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_close -> dialog?.dismiss()
        }
    }

}