package com.toptal.calories.ui.main.entries_list

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.toptal.calories.R
import com.toptal.calories.data.model.Day
import com.toptal.calories.data.model.FoodEntry
import com.toptal.calories.databinding.EntryListFragmentLayoutBinding
import com.toptal.calories.utils.*
import com.toptal.calories.utils.base.BaseFragment
import java.util.*
import kotlin.collections.HashMap

class EntryListFragment : BaseFragment() {

    private var _binding: EntryListFragmentLayoutBinding? = null
    private var mViewModel: EntryListViewModel? = null
    private val binding get() = _binding!!

    //date filters
    var fromSelectDate: Calendar? = null
    var toSelectDate: Calendar? = null
    var isFromPicked = false
    var isToPicked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EntryListFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toggleLoading(true)
        mViewModel = ViewModelProvider(this).get(EntryListViewModel::class.java)
        initViews()
        initListeners()
        observe()
        mViewModel?.getUserEntries(FirebaseAuth.getInstance().currentUser?.uid!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun observe() {
        mViewModel?.getFoodEntriesObservable?.observe(viewLifecycleOwner) { list ->
            val daysList = mutableListOf<Day>()
            val map = HashMap<String, MutableList<FoodEntry>>()
            list.forEach { item ->
                if (!map.containsKey(getStringFromDate(item.entry_date!!))) {
                    map[getStringFromDate(item.entry_date!!)] = mutableListOf()
                    map[getStringFromDate(item.entry_date!!)]?.add(item)
                } else {
                    map[getStringFromDate(item.entry_date!!)]?.add(item)
                }
            }

            map.keys.forEach { key ->
                val day = Day()
                day.dateString = key
                day.entriesList = map[key]!!
                daysList.add(day)
            }

            (binding.felRv.adapter as DaysAdapter).updateList(
                if (daysList.isNotEmpty())
                    daysList.sortedBy { item ->
                        getDateFromString(
                            item.dateString
                        )
                    } as MutableList<Day> else daysList)
            toggleLoading(false)
        }
        mViewModel?.errorObservable?.observe(viewLifecycleOwner) {
            toggleLoading(false)
        }
    }

    override fun toggleLoading(isLoading: Boolean) {
        binding.felProgress.visibility = getViewVisibility(isLoading)
    }

    override fun initViews() {
        initRecycler()
        initFilterViews()
    }

    fun initFilterViews() {
        if (hasFromFilter()) {
            binding.felFilterFromTv.text = getFromFilter()?.let { getStringFromDate(it) }
            binding.felFilterClearBtn.visibility = getViewVisibility(true)
        } else {
            binding.felFilterFromTv.text = getString(R.string.from)
        }

        if (hasToFilter()) {
            binding.felFilterToTv.text = getToFilter()?.let { getStringFromDate(it) }
            binding.felFilterClearBtn.visibility = getViewVisibility(true)
        } else {
            binding.felFilterToTv.text = getString(R.string.to)
        }

        if (!hasFromFilter() && !hasToFilter()) {
            binding.felFilterClearBtn.visibility = getViewVisibility(false)
        }
    }

    val dateListener =
        DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            toggleLoading(true)
            if (isFromPicked) {
                fromSelectDate = Calendar.getInstance()
                fromSelectDate?.set(year, month, dayOfMonth)
                fromSelectDate?.time?.let {
                    binding.felFilterFromTv.text = getStringFromDate(it)
                    setFromFilter(it)
                    binding.felFilterClearBtn.visibility = getViewVisibility(true)
                }
            }

            if (isToPicked) {
                toSelectDate = Calendar.getInstance()
                toSelectDate?.set(year, month, dayOfMonth)
                toSelectDate?.time?.let {
                    binding.felFilterToTv.text = getStringFromDate(it)
                    setToFilter(it)
                    binding.felFilterClearBtn.visibility = getViewVisibility(true)
                }
            }
            mViewModel?.getUserEntries(FirebaseAuth.getInstance().currentUser?.uid!!)
        }

    override fun initListeners() {
        binding.felFab.setOnClickListener {
            if (requireContext().isNetworkAvailable()) {
                findNavController().navigate(R.id.entries_to_add_action)
            } else {
                showSnackMessage(requireView(), "Network unavailable, try again later.")
            }

        }

        binding.felFilterFromTv.setOnClickListener {
            isFromPicked = true
            isToPicked = false
            DatePickerDialog(
                requireContext(),
                dateListener,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.felFilterToTv.setOnClickListener {
            isFromPicked = false
            isToPicked = true
            DatePickerDialog(
                requireContext(),
                dateListener,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.felFilterClearBtn.setOnClickListener {
            toggleLoading(true)
            fromSelectDate = null
            toSelectDate = null
            clearFromFilter()
            clearToFilter()
            binding.felFilterFromTv.text = getString(R.string.from)
            binding.felFilterToTv.text = getString(R.string.to)
            it.visibility = getViewVisibility(false)
            mViewModel?.getUserEntries(FirebaseAuth.getInstance().currentUser?.uid!!)
        }
    }

    fun initRecycler() {
        if (binding.felRv.layoutManager == null) binding.felRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        if (binding.felRv.adapter == null) binding.felRv.adapter = DaysAdapter()
    }

    override fun onResume() {
        super.onResume()
        mViewModel?.getUserEntries(FirebaseAuth.getInstance().currentUser?.uid!!)
    }
}