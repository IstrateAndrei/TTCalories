package com.toptal.calories.ui.admin

import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.toptal.calories.R
import com.toptal.calories.databinding.AdminActivityLayoutBinding
import com.toptal.calories.utils.base.BaseActivity

class AdminActivity : BaseActivity() {

    private var _binding: AdminActivityLayoutBinding? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = AdminActivityLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    fun initViews() {
        if (binding.aaViewpager.adapter == null) binding.aaViewpager.adapter =
            AdminPagerAdapter(this)
        TabLayoutMediator(
            binding.aaTabLayout, binding.aaViewpager
        ) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.entries_label)
                }
                else -> {
                    tab.text = getString(R.string.reports)
                }
            }
        }.attach()
    }
}