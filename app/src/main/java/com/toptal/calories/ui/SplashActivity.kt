package com.toptal.calories.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.toptal.calories.databinding.SplashActivityLayoutBinding
import com.toptal.calories.ui.auth.AuthActivity
import com.toptal.calories.utils.base.BaseActivity
import com.toptal.calories.utils.isUserLoggedIn
import com.toptal.calories.utils.openMainScreen

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = SplashActivityLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        openApp()
    }

    fun openApp() {
        Handler(Looper.getMainLooper()).postDelayed({
            //skip Auth screen if user logged in
            if (isUserLoggedIn()) {
                this.openMainScreen()
            } else {
                startActivity(Intent(this, AuthActivity::class.java))
                this.finish()
            }
        }, 2000)
    }
}