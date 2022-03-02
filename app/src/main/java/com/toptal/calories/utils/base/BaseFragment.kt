package com.toptal.calories.utils.base

import androidx.fragment.app.Fragment
import com.toptal.calories.utils.closeKeyboard

abstract class BaseFragment : Fragment() {

    abstract fun observe()
    abstract fun toggleLoading(isLoading: Boolean)
    abstract fun initViews()
    abstract fun initListeners()
    override fun onDetach() {
        super.onDetach()
        requireActivity().closeKeyboard(requireActivity().currentFocus)
    }

}