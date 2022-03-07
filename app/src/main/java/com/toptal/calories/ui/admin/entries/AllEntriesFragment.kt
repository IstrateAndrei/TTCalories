package com.toptal.calories.ui.admin.entries

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.toptal.calories.R
import com.toptal.calories.data.model.FoodEntry
import com.toptal.calories.databinding.AdminEntriesFragmentLayoutBinding
import com.toptal.calories.ui.admin.AdminActivity
import com.toptal.calories.ui.main.add_entry.AddEntryFragment
import com.toptal.calories.ui.main.entries_list.EntriesAdapter
import com.toptal.calories.utils.*
import com.toptal.calories.utils.base.BaseFragment
import java.util.*

class AllEntriesFragment : BaseFragment() {

    private var _binding: AdminEntriesFragmentLayoutBinding? = null
    private val binding get() = _binding!!

    //date filters
    var fromSelectDate: Calendar? = null
    var toSelectDate: Calendar? = null
    var isFromPicked = false
    var isToPicked = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AdminEntriesFragmentLayoutBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toggleLoading(true)
        initViews()
        initListeners()
        observe()
        (requireActivity() as AdminActivity).adminViewModel.getAdminEntries()
    }

    override fun observe() {
        (requireActivity() as AdminActivity).adminViewModel.entriesObservable.observe(
            viewLifecycleOwner
        ) { list ->
            (binding.aefRv.adapter as EntriesAdapter).updateList(list.sortedBy { item -> item.entry_date } as MutableList<FoodEntry>)
            toggleLoading(false)
        }
        (requireActivity() as AdminActivity).adminViewModel.deleteEntryObservable.observe(
            viewLifecycleOwner
        ) { result ->
            showSnackMessage(requireView(), getString(R.string.entry_deleted))
            (binding.aefRv.adapter as EntriesAdapter).removeEntry(result.first, result.second)
            toggleLoading(false)
        }
        (requireActivity() as AdminActivity).adminViewModel.errorObservable.observe(
            viewLifecycleOwner
        ) {
            toggleLoading(false)
        }
    }

    override fun toggleLoading(isLoading: Boolean) {
        binding.aefProgress.visibility = getViewVisibility(isLoading)
    }

    override fun initViews() {
        initRecycler()
        initFilterViews()
    }

    fun initRecycler() {
        if (binding.aefRv.layoutManager == null) binding.aefRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        if (binding.aefRv.adapter == null) binding.aefRv.adapter = EntriesAdapter()
    }

    fun initFilterViews() {
        if (hasFromFilter()) {
            binding.aefFilterFromTv.text = getFromFilter()?.let { getStringFromDate(it) }
            binding.aefFilterClearBtn.visibility = getViewVisibility(true)
        } else {
            binding.aefFilterFromTv.text = getString(R.string.from)
        }

        if (hasToFilter()) {
            binding.aefFilterToTv.text = getToFilter()?.let { getStringFromDate(it) }
            binding.aefFilterClearBtn.visibility = getViewVisibility(true)
        } else {
            binding.aefFilterToTv.text = getString(R.string.to)
        }

        if (!hasFromFilter() && !hasToFilter()) {
            binding.aefFilterClearBtn.visibility = getViewVisibility(false)
        }
    }


    override fun initListeners() {

        (binding.aefRv.adapter as EntriesAdapter).setupListener(object :
            EntriesAdapter.OnEntryClickListener {
            override fun onEntryClicked(item: FoodEntry, position: Int) {
                val fragment = AddEntryFragment()
                val bundle = Bundle()
                bundle.putParcelable(Constants.BUNDLE_ADMIN_ITEM_KEY, item)
                fragment.arguments = bundle
                requireActivity().supportFragmentManager.beginTransaction()
                    .add(binding.aefFrameContainer.id, fragment)
                    .addToBackStack(AddEntryFragment::class.java.simpleName).commit()
            }

            override fun onEntryDeleted(item: FoodEntry, position: Int) {
                toggleLoading(true)
                (requireActivity() as AdminActivity).adminViewModel.deleteEntry(item, position)
            }
        })

        binding.aefFab.setOnClickListener {

            if (requireContext().isNetworkAvailable()) {
                val fragment = AddEntryFragment()
                val bundle = Bundle()
                bundle.putBoolean(Constants.BUNDLE_ADMIN_ADD_KEY, true)
                fragment.arguments = bundle
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(binding.aefFrameContainer.id, fragment)
                    .addToBackStack(AddEntryFragment::class.java.simpleName).commit()
            } else {
                showSnackMessage(requireView(), "Network unavailable, try again later.")
            }
        }

        binding.aefFilterFromTv.setOnClickListener {
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

        binding.aefFilterToTv.setOnClickListener {
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

        binding.aefFilterClearBtn.setOnClickListener {
            toggleLoading(true)
            fromSelectDate = null
            toSelectDate = null
            clearFromFilter()
            clearToFilter()
            binding.aefFilterFromTv.text = getString(R.string.from)
            binding.aefFilterToTv.text = getString(R.string.to)
            it.visibility = getViewVisibility(false)
            (requireActivity() as AdminActivity).adminViewModel.getAdminEntries()
        }

    }

    val dateListener =
        DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            toggleLoading(true)
            if (isFromPicked) {
                fromSelectDate = Calendar.getInstance()
                fromSelectDate?.set(year, month, dayOfMonth)
                fromSelectDate?.time?.let {
                    binding.aefFilterFromTv.text = getStringFromDate(it)
                    setFromFilter(it)
                    binding.aefFilterClearBtn.visibility = getViewVisibility(true)
                }
            }

            if (isToPicked) {
                toSelectDate = Calendar.getInstance()
                toSelectDate?.set(year, month, dayOfMonth)
                toSelectDate?.time?.let {
                    binding.aefFilterToTv.text = getStringFromDate(it)
                    setToFilter(it)
                    binding.aefFilterClearBtn.visibility = getViewVisibility(true)
                }
            }
            (requireActivity() as AdminActivity).adminViewModel.getAdminEntries()
        }

}