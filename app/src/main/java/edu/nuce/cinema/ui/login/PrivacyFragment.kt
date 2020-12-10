package edu.nuce.cinema.ui.login

import android.os.Bundle
import android.view.View
import edu.nuce.base.delegates.viewBinding
import edu.nuce.cinema.R
import edu.nuce.cinema.databinding.FragmentPrivacyBinding
import edu.nuce.cinema.ui.base.BaseFragment

class PrivacyFragment : BaseFragment(R.layout.fragment_privacy),View.OnClickListener {
    private val _binding: FragmentPrivacyBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding.appbar.imgAction.setOnClickListener(this)
        _binding.appbar.title.setText(arguments?.getString("title"))
        _binding.tvUserService.setText(arguments?.getString("content"))
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.img_action -> requireActivity().onBackPressed()
        }
    }

}