package edu.nuce.cinema.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.nuce.base.delegates.viewBinding
import edu.nuce.base.view.BaseDialogFragment
import edu.nuce.cinema.R
import edu.nuce.cinema.databinding.DialogRatingBinding
import edu.nuce.cinema.ui.detail.fragments.description.DescriptionEvents

class RatingDialog(
    private val rating: Float,
    private val events: DescriptionEvents
) : BaseDialogFragment(), View.OnClickListener {

    private val _binding by viewBinding<DialogRatingBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_rating, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding.apply {
            btnClose.setOnClickListener(this@RatingDialog)
            btnSubmit.setOnClickListener {
                events.onRating(ratingBar.rating)
                dialog?.dismiss()
            }
            ratingBar.rating = rating
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