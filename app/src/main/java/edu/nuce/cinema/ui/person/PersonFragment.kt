package edu.nuce.cinema.ui.person

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.facebook.CallbackManager
import dagger.hilt.android.AndroidEntryPoint
import edu.nuce.base.auth.RxOAuthManager
import edu.nuce.base.delegates.viewBinding
import edu.nuce.base.mvi.MviView
import edu.nuce.cinema.R
import edu.nuce.cinema.databinding.FragmentPersonBinding
import edu.nuce.cinema.ui.base.BaseFragment
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class PersonFragment : BaseFragment(R.layout.fragment_person),
    MviView<PersonIntent, PersonViewState> {

    @Inject
    lateinit var oAuthManager: RxOAuthManager

    @Inject
    lateinit var requestManager: RequestManager
    private val _binding: FragmentPersonBinding by viewBinding()
    private lateinit var _callbackManager: CallbackManager
    private val _getAchievementIntentPublisher =
        PublishSubject.create<PersonIntent.GetAchievementIntent>()
    private val _getUserIntentPublisher = PublishSubject.create<PersonIntent.GetUserIntent>()
    private val _viewModel: PersonViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        if (oAuthManager.isExistsCredentials) {
            _getUserIntentPublisher.onNext(PersonIntent.GetUserIntent)
            _getAchievementIntentPublisher.onNext(PersonIntent.GetAchievementIntent)
        }
    }

    private fun bind() {
        _viewModel.states().subscribe(this::render).disposeOnDestroy()
        _viewModel.processIntents(intents())
    }

    override fun intents(): Observable<PersonIntent> {
        return Observable.merge(
            _getAchievementIntentPublisher,
            _getUserIntentPublisher
        )
    }

    fun event(rxOAuthManager: RxOAuthManager) {
        if (rxOAuthManager.isExistsCredentials) {
            rxOAuthManager.clearCredentials()
            _binding.tvLogout.text = resources.getString(R.string.login)
        } else {
            findNavController().navigate(PersonFragmentDirections.actionPersonToLogin())
        }
    }

    override fun render(state: PersonViewState) {
        if (state.error != null) {
            Timber.e(state.error)
            return
        }
        Timber.e(state.isLoading.toString())
        _binding.apply {
            state.user?.let {
                requestManager.load(it.avatar)
                    .circleCrop()
                    .into(ivAvatar)
                tvName.text = it.lastName + " " + it.firstName
                ivAchievement.layoutParams.width =
                    root.resources.getDimension(R.dimen._50sdp).toInt()
                ivAchievement.layoutParams.height =
                    root.resources.getDimension(R.dimen._20sdp).toInt()
                requestManager.load(state.achievement?.image)
                    .into(ivAchievement)
            }
            if (oAuthManager.isExistsCredentials) {
                tvLogout.text = root.resources.getString(R.string.logout)
            } else {
                tvLogout.text = root.resources.getString(R.string.login)
            }
            tvLogout.setOnClickListener { event(rxOAuthManager = oAuthManager) }
            tvName.setOnClickListener {
                if (!oAuthManager.isExistsCredentials) {
                    findNavController().navigate(PersonFragmentDirections.actionPersonToLogin())
                }
            }
        }
    }
}