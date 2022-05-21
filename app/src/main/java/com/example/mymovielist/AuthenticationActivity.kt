package com.example.mymovielist

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mymovielist.databinding.ActivityAuthenticationBinding
import com.example.mymovielist.ui.login.AuthenticationViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class AuthenticationActivity : AppCompatActivity() {
    private val viewModel by viewModels<AuthenticationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAuthenticationBinding.inflate(layoutInflater)

        binding.loginButton.setOnClickListener { launchSignInFlow() }

        setContentView(binding.root)

        viewModel.authenticationState.observe(this, { authenticationState ->
            when (authenticationState) {
                AuthenticationViewModel.AuthenticationState.AUTHENTICATED -> startActivity(Intent(this, MainActivity::class.java))
                else -> Log.e(
                    TAG,
                    "Authentication state that doesn't require any UI change $authenticationState"
                )
            }
        })
    }

    private val signInLauncher = registerForActivityResult(FirebaseAuthUIActivityResultContract()) { result ->
        this.onSignInResult(result)
    }

    private fun launchSignInFlow() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.Theme_MyMovieList)
            .setIsSmartLockEnabled(!BuildConfig.DEBUG, true)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in user.
            Log.i(
                TAG,
                "Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!"
            )
        } else {
            // Sign in failed. If response is null the user canceled the sign-in flow using
            // the back button. Otherwise check response.getError().getErrorCode() and handle
            // the error.
            Log.i(TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
        }
    }
}