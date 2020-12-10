package edu.nuce.cinema.ui.splash

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.nuce.base.delegates.viewBinding
import edu.nuce.cinema.R
import edu.nuce.cinema.databinding.FragmentSplashBinding
import edu.nuce.cinema.ui.base.BaseFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    private val _binding: FragmentSplashBinding by viewBinding()

    //    @Inject
//    lateinit var userManager: UserManager
//    private lateinit var userDataRepository: UserDataRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requireActivity().bottom_nav.visibility = View.GONE

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//
//        if (userManager.isUserLoggedIn()) {
//            userDataRepository = EntryPoints.get(
//                userManager.userComponent!!, UserSessionComponentEntryPoint::class.java
//            ).userDataRepository()
//            userDataRepository
//                .getAuthUser()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                    { userDataRepository.cacheUser(it) },
//                    { error -> Timber.e(error) }
//                ).disposeOnDestroy()
//        }
        Observable.timer(3, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { findNavController().navigate(R.id.action_splashFragment_to_homeFragment) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}