package com.toptal.calories.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


fun Activity.closeKeyboard(view: View?) {
    val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE)
    view?.let {
        (inputManager as InputMethodManager).hideSoftInputFromWindow(it.windowToken, 0)
    }
}