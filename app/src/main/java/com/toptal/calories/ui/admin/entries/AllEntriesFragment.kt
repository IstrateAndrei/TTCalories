package com.toptal.calories.ui.admin.entries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.toptal.calories.databinding.AdminEntriesFragmentLayoutBinding
import com.toptal.calories.ui.main.entries_list.EntriesAdapter
import com.toptal.calories.utils.base.BaseFragment
import com.toptal.calories.utils.getViewVisibility

class AllEntriesFragment : BaseFragment() {

    private lateinit var adminViewModel: AllEntriesViewModel

    private var _binding: AdminEntriesFragmentLayoutBinding? = null
    private val binding get() = _binding!!

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
        adminViewModel = ViewModelProvider(this).get(AllEntriesViewModel::class.java)
        toggleLoading(true)
        initViews()
        initListeners()
        observe()
        adminViewModel.getAllEntries()
    }

    override fun observe() {
        adminViewModel.allEntriesObservable.observe(viewLifecycleOwner, Observer { list ->
            if (list.isNotEmpty()) {
                (binding.aefRv.adapter as EntriesAdapter).updateList(list)
            }
            toggleLoading(false)
        })
    }

    override fun toggleLoading(isLoading: Boolean) {
        binding.aefProgress.visibility = getViewVisibility(isLoading)
    }

    override fun initViews() {

        if (binding.aefRv.layoutManager == null) binding.aefRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        if (binding.aefRv.adapter == null) binding.aefRv.adapter = EntriesAdapter()

    }

    override fun initListeners() {

    }
}