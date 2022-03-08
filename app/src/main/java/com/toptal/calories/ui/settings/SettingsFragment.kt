package com.toptal.calories.ui.settings

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.orhanobut.hawk.Hawk
import com.toptal.calories.R
import com.toptal.calories.databinding.SettingsFragmentLayoutBinding
import com.toptal.calories.utils.Constants
import com.toptal.calories.utils.base.BaseFragment

class SettingsFragment : BaseFragment() {


    private var _binding: SettingsFragmentLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SettingsFragmentLayoutBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
        observe()
    }

    override fun observe() {

    }

    override fun toggleLoading(isLoading: Boolean) {

    }

    override fun initViews() {
        val currentLimit = Hawk.get<Int>(Constants.HAWK_CALORIC_LIMIT_KEY)
        binding.sfLimitValueTv.text = currentLimit.toString()
    }

    override fun initListeners() {
        binding.sfChangeEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.sfSetBtn.isEnabled = p0?.isNotEmpty() == true
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        binding.sfSetBtn.setOnClickListener {
            val newValue = binding.sfChangeEt.text.toString().toInt()

            Hawk.put(Constants.HAWK_CALORIC_LIMIT_KEY, newValue)
            Snackbar.make(it, getString(R.string.caloric_limit_changed), Snackbar.LENGTH_SHORT)
                .show()

            binding.sfLimitValueTv.text = newValue.toString()
            binding.sfChangeEt.setText("")
            binding.sfChangeEt.clearFocus()
            binding.sfChangeTil.clearFocus()
        }
    }


}