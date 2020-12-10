package edu.nuce.cinema.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import edu.nuce.base.delegates.viewBinding
import edu.nuce.base.mvi.MviView
import edu.nuce.cinema.R
import edu.nuce.cinema.data.models.Login
import edu.nuce.cinema.databinding.FragmentLoginBinding
import edu.nuce.cinema.ui.base.BaseFragment
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : BaseFragment(R.layout.fragment_login), View.OnClickListener,
    MviView<LoginIntent, LoginViewState> {

    private val _binding: FragmentLoginBinding by viewBinding()
    private lateinit var _googleSignInClient: GoogleSignInClient
    private lateinit var _callbackManager: CallbackManager
    private val _getDefaultOAuthCredentials =
        PublishSubject.create<LoginIntent.LoginWithGoogleIntent>()
    private val _getUserIntentPublisher = PublishSubject.create<LoginIntent.GetUserIntent>()
    private val _viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        val googleOptions: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        _googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleOptions)
        _googleSignInClient.signOut()
        _callbackManager = CallbackManager.Factory.create()
        _binding.google.setOnClickListener(this)
        _binding.facebook.setOnClickListener(this)
        _binding.privacyAgreement.setOnClickListener(this)
        _binding.privacyPolicy.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.google -> signInGoogle()
            R.id.facebook -> signInFacebook()
            R.id.privacy_agreement -> changeFragment(
                "Privacy agreement",
                resources.getString(R.string.privacy_agreement_content)
            )
            R.id.privacy_policy -> changeFragment(
                "Privacy policy",
                resources.getString(R.string.privacy_agreement_content)
            )
        }
    }

    private fun changeFragment(title: String, content: String) {
        val bundle = bundleOf("title" to title, "content" to content)
        Navigation.findNavController(requireView())
            .navigate(R.id.action_loginFragment_to_privacyFragment, bundle)
    }

    private fun signInFacebook() {
        LoginManager.getInstance()
            .logInWithReadPermissions(this, listOf("email", "user_friends", "public_profile"))
        LoginManager.getInstance()
            .registerCallback(_callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
//                    val accessToken = result?.accessToken?.token
                    val request: GraphRequest = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken()
                    ) { _, response ->
//                        if(BuildConfig.DEBUG){
//                            FacebookSdk.setIsDebugEnabled(true)
//                            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
//                            val profile:Profile = Profile.getCurrentProfile()
//                            Timber.e(profile.firstName)
//                            Timber.e(profile.lastName)
//                            Timber.e(accessToken)
//                        }
                        Timber.e(response.toString())
                    }
                    request.executeAsync()
                }

                override fun onCancel() {
                }

                override fun onError(error: FacebookException?) {
                    Timber.e(error?.message!!)
                }

            })
    }

    private fun signInGoogle() {
        val signInIntent: Intent = _googleSignInClient.signInIntent
        startActivityForResult(
            signInIntent,
            RC_SIGN_IN
        )
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            updateUI(account)
        } catch (e: ApiException) {
            Timber.e("signInResult:failed code=%s", e.statusCode)
            updateUI(null)
        }
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        account?.let {
            // Call api register account
//            Timber.e(it.givenName)
//            Timber.e(it.familyName)
//            Timber.e(it.email)
//            Timber.e(it.photoUrl.toString())
//            Timber.e(it.id)
            _getDefaultOAuthCredentials.onNext(LoginIntent.LoginWithGoogleIntent(Login(it.email.toString(),it.givenName.toString(),
                it.familyName.toString(),
                it.id.toString(),
                "",
                it.photoUrl.toString()
            )))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        } else {
            _callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding.google.setOnClickListener(null)
        _binding.facebook.setOnClickListener(null)
    }

    companion object {
        const val RC_SIGN_IN = 2020
    }

    private fun bind() {
        _viewModel.states().subscribe(this::render).disposeOnDestroy()
        _viewModel.processIntents(intents())
    }

    override fun intents(): Observable<LoginIntent> {
        return Observable.merge(
            _getDefaultOAuthCredentials,
            _getUserIntentPublisher
        )
    }

    override fun render(state: LoginViewState) {
        if (state.error != null) {
            Timber.e(state.error)
            return
        }
        Timber.e(state.isLoading.toString())
        Timber.e(state.defaultOAuthCredentials.toString())
    }
}