package com.toptal.calories.ui.auth

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.toptal.calories.R
import com.toptal.calories.utils.base.BaseActivity
import com.toptal.calories.utils.isUserLoggedIn
import com.toptal.calories.utils.openMainScreen

class AuthActivity : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //skip Auth screen if user logged in
        if (isUserLoggedIn()) {
            this.openMainScreen()
            return
        }

        setContentView(R.layout.activity_login)
        navController = findNavController(R.id.nav_host_fragment_content_login)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

}