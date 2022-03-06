package com.toptal.calories.ui.admin.reports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.toptal.calories.data.model.AverageCals
import com.toptal.calories.data.model.FoodEntry
import com.toptal.calories.databinding.AdminReportsFragmentLayoutBinding
import com.toptal.calories.ui.admin.AdminActivity
import com.toptal.calories.utils.base.BaseFragment
import com.toptal.calories.utils.getDaysAgoDate
import com.toptal.calories.utils.getViewVisibility
import java.util.*

class ReportDataFragment : BaseFragment() {

    private var _binding: AdminReportsFragmentLayoutBinding? = null
    private val binding get() = _binding!!

    private val lastWeekList = mutableListOf<FoodEntry>()
    private val avgCalList = mutableListOf<AverageCals>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AdminReportsFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toggleLoading(true)
        initViews()
        initListeners()
        observe()
        (requireActivity() as AdminActivity).adminViewModel.getAllEntries()
    }

    override fun observe() {
        (requireActivity() as AdminActivity).adminViewModel.allEntriesObservable.observe(
            viewLifecycleOwner
        ) { list ->
            val lastWeekEntries =
                list.filter { item ->
                    item.entry_date!!.before(Calendar.getInstance().time) && item.entry_date!!.after(
                        getDaysAgoDate(7)
                    )
                }

            val lastTwoWeeksCount = list.filter { item ->
                item.entry_date!!.before(Calendar.getInstance().time) && item.entry_date!!.after(
                    getDaysAgoDate(14)
                )
            }.count()

            val theWeekBeforeCounts = list.filter { item ->
                item.entry_date!!.after(getDaysAgoDate(14)) && item.entry_date!!.before(
                    getDaysAgoDate(7)
                )
            }.count()

            lastWeekList.clear()
            lastWeekList.addAll(lastWeekEntries)

            binding.arfFirstWeekValueTv.text = lastWeekEntries.count().toString()
            binding.arfSecondWeekValueTv.text = lastTwoWeeksCount.toString()
            binding.arfWeekBeforeValueTv.text = theWeekBeforeCounts.toString()

            //handle average number of calories per user for the last 7 days
            (requireActivity() as AdminActivity).adminViewModel.getAllUsers()
        }
        (requireActivity() as AdminActivity).adminViewModel.allUsersObservable.observe(
            viewLifecycleOwner
        ) { userList ->
            avgCalList.clear()
            userList.forEach { user ->
                val avgCal = AverageCals()
                avgCal.userName = user.name
                val uEntries = lastWeekList.filter { item -> item.creator_id == user.user_id }
                var sum = 0
                uEntries.forEach { item ->
                    sum += item.calories
                }
                avgCal.avg = if (sum == 0 && uEntries.count() == 0) 0 else sum / uEntries.count()
                avgCalList.add(avgCal)
            }
            (binding.arfAvgRv.adapter as AverageCalAdapter).updateList(avgCalList)
            toggleLoading(false)
        }
    }

    override fun toggleLoading(isLoading: Boolean) {
        binding.arfProgress.visibility = getViewVisibility(isLoading)
    }

    override fun initViews() {
        if (binding.arfAvgRv.layoutManager == null) binding.arfAvgRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        if (binding.arfAvgRv.adapter == null) binding.arfAvgRv.adapter = AverageCalAdapter()
    }

    override fun initListeners() {

    }
}