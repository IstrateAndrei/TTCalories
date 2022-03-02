package com.toptal.calories.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toptal.calories.databinding.FragmentUserLoginBinding
import com.toptal.calories.utils.base.BaseFragment

class UserLoginFragment : BaseFragment() {

    var _binding: FragmentUserLoginBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserLoginBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun observe() {
        TODO("Not yet implemented")
    }

    override fun toggleLoading(isLoading: Boolean) {
        TODO("Not yet implemented")
    }

    override fun initViews() {
        TODO("Not yet implemented")
    }

    override fun initListeners() {
        TODO("Not yet implemented")
    }
}