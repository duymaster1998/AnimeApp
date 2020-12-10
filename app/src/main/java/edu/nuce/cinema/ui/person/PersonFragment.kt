package edu.nuce.cinema.ui.person

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import edu.nuce.base.delegates.viewBinding
import edu.nuce.cinema.R
import edu.nuce.cinema.databinding.FragmentPersonBinding
import edu.nuce.cinema.ui.base.BaseFragment

@AndroidEntryPoint
class PersonFragment : BaseFragment(R.layout.fragment_person) {

    private val _binding: FragmentPersonBinding by viewBinding()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}