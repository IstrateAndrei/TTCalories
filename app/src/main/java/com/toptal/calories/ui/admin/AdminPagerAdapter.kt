package com.toptal.calories.ui.admin

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.toptal.calories.ui.admin.entries.AllEntriesFragment
import com.toptal.calories.ui.admin.reports.ReportDataFragment

class AdminPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {


    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) AllEntriesFragment()
        else ReportDataFragment()
    }
}