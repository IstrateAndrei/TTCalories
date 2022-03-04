package com.toptal.calories

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.toptal.calories.data.model.User
import com.toptal.calories.databinding.ActivityMainBinding
import com.toptal.calories.ui.admin.AdminActivity
import com.toptal.calories.ui.auth.AuthActivity

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    lateinit var optionsMenu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        getUserRole(FirebaseAuth.getInstance().currentUser?.uid!!)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        optionsMenu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
//                todo move to settings screen
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.entries_to_settings_action)
            }
            R.id.action_filters -> {
                //todo move to filters screen
            }
            R.id.action_logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, AuthActivity::class.java))
                this.finish()
            }

            R.id.action_admin -> {
                startActivity(Intent(this, AdminActivity::class.java))
            }
        }

        return NavigationUI.onNavDestinationSelected(
            item,
            navController
        ) || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun getUserRole(uid: String) {
        val db = FirebaseFirestore.getInstance()
        val usersEntryRef = db.collection("users").document(uid)
        usersEntryRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val queryDocSnapshot = task.result
                val user = queryDocSnapshot.toObject(User::class.java)
                if (user?.admin!!) {
                    val adminMenuItem = optionsMenu.findItem(R.id.action_admin)
                    adminMenuItem.isVisible = user.admin
                }
            }
        }

    }
}