package com.toptal.calories.utils

import android.app.Activity
import android.content.Context
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar
import com.toptal.calories.R


fun Activity.closeKeyboard(view: View?) {
    val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE)
    view?.let {
        (inputManager as InputMethodManager).hideSoftInputFromWindow(it.windowToken, 0)
    }
}


fun matchesEmailPattern(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun getViewVisibility(isVisible: Boolean) = if (isVisible) View.VISIBLE else View.GONE

fun showSnackMessage(view: View, message: String) {
    Snackbar.make(
        view,
        message,
        Snackbar.LENGTH_SHORT
    ).show()
}