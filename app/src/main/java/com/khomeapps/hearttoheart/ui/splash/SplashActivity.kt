package com.khomeapps.hearttoheart.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.khomeapps.hearttoheart.ui.dashboard.DashboardActivity

class SplashActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this,DashboardActivity::class.java))
        finish()
    }
}