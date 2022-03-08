package com.toptal.calories.ui.admin

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.toptal.calories.R
import com.toptal.calories.databinding.AdminActivityLayoutBinding
import com.toptal.calories.ui.admin.entries.AdminViewModel
import com.toptal.calories.utils.base.BaseActivity

class AdminActivity : BaseActivity() {

    private var _binding: AdminActivityLayoutBinding? = null
    val binding get() = _binding!!

    lateinit var adminViewModel: AdminViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = AdminActivityLayoutBinding.inflate(layoutInflater)
        adminViewModel = ViewModelProvider(this).get(AdminViewModel::class.java)
        setContentView(binding.root)
        initViews()
        initListeners()
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

    fun initListeners() {
        binding.aaViewpager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 1) adminViewModel.getAdminEntries()
            }
        })
    }
}