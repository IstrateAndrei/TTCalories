package com.toptal.calories.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.orhanobut.hawk.Hawk
import com.toptal.calories.R
import com.toptal.calories.data.model.User
import com.toptal.calories.databinding.ActivityMainBinding
import com.toptal.calories.databinding.FilterDateDialogLayoutBinding
import com.toptal.calories.ui.admin.AdminActivity
import com.toptal.calories.ui.auth.AuthActivity
import com.toptal.calories.utils.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    //date filters
    var fromSelectDate: Calendar? = null
    var toSelectDate: Calendar? = null
    var isFromPicked = false
    var isToPicked = false


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
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.entries_to_settings_action)
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
        FirebaseFirestore.getInstance().collection("users").whereEqualTo("user_id", uid).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val queryDocSnapshot = task.result
                    val user = queryDocSnapshot.toObjects(User::class.java)[0]
                    optionsMenu.findItem(R.id.action_admin).isVisible = user?.admin == true
                    setAdminRights(user?.admin == true)
                }
            }
    }

    fun showFilterDialog() {
        val builder = AlertDialog.Builder(this)
        val dialogBinding = FilterDateDialogLayoutBinding.inflate(layoutInflater)
        builder.setView(dialogBinding.root)

        if (hasFromFilter()) {
            if (fromSelectDate == null) fromSelectDate = Calendar.getInstance()
            fromSelectDate?.time = getFromFilter()
            dialogBinding.fddFromValueTv.text = getFromFilter()?.let { getStringFromDate(it) }
            dialogBinding.fddFromClearBtn.isEnabled = true
        }

        if (hasToFilter()) {
            if (toSelectDate == null) toSelectDate = Calendar.getInstance()
            toSelectDate?.time = getToFilter()
            dialogBinding.fddToValueTv.text = getToFilter()?.let { getStringFromDate(it) }
            dialogBinding.fddToClearBtn.isEnabled = true
        }
        val dialog = builder.create()
        dialog.show()

        val dateListener =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                dialogBinding.fddApplyBtn.isEnabled = true
                if (isFromPicked) {
                    fromSelectDate = Calendar.getInstance()
                    fromSelectDate?.set(year, month, dayOfMonth)
                    fromSelectDate?.time?.let {
                        dialogBinding.fddFromValueTv.text = getStringFromDate(it)
                        dialogBinding.fddFromClearBtn.isEnabled = true
                        dialogBinding.fddApplyBtn.isEnabled = true
                    }
                }

                if (isToPicked) {
                    toSelectDate = Calendar.getInstance()
                    toSelectDate?.set(year, month, dayOfMonth)
                    toSelectDate?.time?.let {
                        dialogBinding.fddToValueTv.text = getStringFromDate(it)
                        dialogBinding.fddToClearBtn.isEnabled = true
                        dialogBinding.fddApplyBtn.isEnabled = true
                    }
                }
            }

        dialogBinding.fddFromClockIv.setOnClickListener {
            isFromPicked = true
            isToPicked = false
            DatePickerDialog(
                this,
                dateListener,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        dialogBinding.fddToClockIv.setOnClickListener {
            isFromPicked = false
            isToPicked = true
            DatePickerDialog(
                this,
                dateListener,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        dialogBinding.fddFromClearBtn.setOnClickListener {
            fromSelectDate = null
            clearFromFilter()
            dialogBinding.fddFromValueTv.text = ""
            if (dialogBinding.fddToValueTv.text.isEmpty()) {
                dialogBinding.fddApplyBtn.isEnabled = false
            }
        }

        dialogBinding.fddToClearBtn.setOnClickListener {
            toSelectDate = null
            clearToFilter()
            dialogBinding.fddToValueTv.text = ""
            if (dialogBinding.fddFromValueTv.text.isEmpty()) {
                dialogBinding.fddApplyBtn.isEnabled = false
            }
        }

        dialogBinding.fddApplyBtn.setOnClickListener {
            fromSelectDate?.time?.let {
                setFromFilter(it)
            }

            toSelectDate?.time?.let {
                setToFilter(it)
            }

            //todo request reload data

            dialog.dismiss()
        }

    }


}