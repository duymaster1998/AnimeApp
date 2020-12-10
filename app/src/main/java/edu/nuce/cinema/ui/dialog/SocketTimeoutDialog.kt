package edu.nuce.cinema.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.nuce.base.delegates.viewBinding
import edu.nuce.base.view.BaseDialogFragment
import edu.nuce.cinema.R
import edu.nuce.cinema.databinding.DialogFragmentSocketTimeoutBinding

class SocketTimeoutDialog(
    private val error: String
) : BaseDialogFragment() {

    private val _binding by viewBinding<DialogFragmentSocketTimeoutBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_fragment_socket_timeout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.tvError.text = error
    }

    companion object {
        fun from(error: String): SocketTimeoutDialog {
            return SocketTimeoutDialog(error)
        }
    }

}