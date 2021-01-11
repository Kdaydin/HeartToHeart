package com.khomeapps.hearttoheart.ui.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.khomeapps.hearttoheart.R
import com.khomeapps.hearttoheart.databinding.ActivitySplashBinding
import com.khomeapps.hearttoheart.ui.base.BaseActivity
import com.khomeapps.hearttoheart.ui.dashboard.DashboardActivity

class SplashActivity : BaseActivity<SplashViewModel, ActivitySplashBinding>() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private val RC_SIGN_IN: Int = 1

    override fun getLayoutRes(): Int = R.layout.activity_splash

    override fun getViewModelType(): SplashViewModel = SplashViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        FirebaseAuth.getInstance().currentUser?.let {
            startActivity(Intent(this, DashboardActivity::class.java))
        }
        // Create and launch sign-in intent
        binding?.btnSignIn?.setOnClickListener {
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setLogo(R.drawable.ic_launcher_foreground) // Set logo drawable
                    .setTheme(R.style.Theme_HTHApp) // Set theme
                    .build(),
                RC_SIGN_IN
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                startActivity(Intent(this, DashboardActivity::class.java))
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }
}