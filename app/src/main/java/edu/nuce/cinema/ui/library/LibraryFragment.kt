package edu.nuce.cinema.ui.library

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import edu.nuce.base.delegates.viewBinding
import edu.nuce.cinema.R
import edu.nuce.cinema.databinding.FragmentLibraryBinding
import edu.nuce.cinema.ui.base.BaseFragment

@AndroidEntryPoint
class LibraryFragment : BaseFragment(R.layout.fragment_library) {

    private val _binding: FragmentLibraryBinding by viewBinding()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}