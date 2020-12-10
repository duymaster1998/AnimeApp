//package edu.nuce.cinema.ui
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import androidx.fragment.app.viewModels
//import com.jakewharton.rxbinding4.view.clicks
//import dagger.hilt.android.AndroidEntryPoint
//import edu.nuce.base.delegates.arg
//import edu.nuce.base.delegates.viewBinding
//import edu.nuce.base.extensions.setExistence
//import edu.nuce.base.extensions.simpleName
//import edu.nuce.cinema.R
//import edu.nuce.cinema.databinding.FragmentTestBinding
//import edu.nuce.cinema.ui.base.BaseFragment
//import timber.log.Timber
//
//@AndroidEntryPoint
//class TestFragment : BaseFragment(R.layout.fragment_test) {
//
//    private val binding: FragmentTestBinding by viewBinding()
//    private val name: String? by arg(simpleName<TestFragment>())
//    private val viewModel: MainViewModel by viewModels()
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.tvHello.text = name
//
//        viewModel.state.subscribe {
//            val state = it.users
//            binding.progress.setExistence(state.isLoading())
//            if (!state.isLoading()) {
//                when {
//                    state.isSuccess() -> if (state.getData()!!.isNotEmpty()) Timber.e(
//                        state.getData().toString()
//                    )
//                    state.isError() -> Timber.e(state.getError())
//                }
//            }
//        }.disposeOnDestroy()
//    }
//
//}