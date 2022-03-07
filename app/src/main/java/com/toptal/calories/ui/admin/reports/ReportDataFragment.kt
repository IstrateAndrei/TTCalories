package com.toptal.calories.ui.admin.reports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
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

    private lateinit var reportViewModel: ReportViewModel
    private val avgCalList = mutableListOf<AverageCals>()

    private val sevenDayList = mutableListOf<FoodEntry>()

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
        reportViewModel = ViewModelProvider(this).get(ReportViewModel::class.java)
        toggleLoading(true)
        initViews()
        initListeners()
        observe()
        reportViewModel.getFirstWeekEntries()
    }

    override fun observe() {

        reportViewModel.firstWeekObservable.observe(viewLifecycleOwner) { list ->
            sevenDayList.clear()
            sevenDayList.addAll(list)
            binding.arfFirstWeekValueTv.text = list.size.toString()
            reportViewModel.getSecondWeekEntries()
        }

        reportViewModel.otherWeekObservable.observe(viewLifecycleOwner) {
            binding.arfWeekBeforeValueTv.text = it.size.toString()
            reportViewModel.getAllUsers()
        }

        reportViewModel.errorObservable.observe(viewLifecycleOwner) {
            toggleLoading(false)
        }

        reportViewModel.allUsersObservable.observe(
            viewLifecycleOwner
        ) { userList ->
            avgCalList.clear()
            userList.forEach { user ->
                val avgCal = AverageCals()
                avgCal.userName = user.name
                val uEntries = sevenDayList.filter { item -> item.creator_id == user.user_id }
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