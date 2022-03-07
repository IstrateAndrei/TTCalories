package com.toptal.calories.utils.base

import androidx.appcompat.app.AppCompatActivity
import com.toptal.calories.utils.closeKeyboard

abstract class BaseActivity : AppCompatActivity() {
//    abstract fun observe()
//    abstract fun toggleLoading(isLoading: Boolean)
//    abstract fun initViews()
//    abstract fun initListeners()
    override fun onDestroy() {
        super.onDestroy()
        this.closeKeyboard(this.currentFocus)
    }
}