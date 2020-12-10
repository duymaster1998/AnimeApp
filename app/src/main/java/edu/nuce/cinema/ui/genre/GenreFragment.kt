package edu.nuce.cinema.ui.genre

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import edu.nuce.base.delegates.viewBinding
import edu.nuce.cinema.R
import edu.nuce.cinema.databinding.FragmentGenreBinding
import edu.nuce.cinema.ui.base.BaseFragment

@AndroidEntryPoint
class GenreFragment : BaseFragment(R.layout.fragment_genre) {

    private val _binding: FragmentGenreBinding by viewBinding()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}