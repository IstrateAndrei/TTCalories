package com.toptal.calories.utils.base

import androidx.appcompat.app.AppCompatActivity
import com.toptal.calories.utils.closeKeyboard

abstract class BaseActivity : AppCompatActivity() {
    override fun onDestroy() {
        super.onDestroy()
        this.closeKeyboard(this.currentFocus)
    }
}