package com.sparta.youandme.view.splash

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.sparta.youandme.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
    }
//    public fun Activity.installSplashScreen(): SplashScreen {
//        val splashScreen = SplashScreen(this)
//        splashScreen.install()
//        return splashScreen
//    }
}