package com.toptal.calories.ui.admin.entries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.toptal.calories.R
import com.toptal.calories.data.model.FoodEntry
import com.toptal.calories.databinding.AdminEntriesFragmentLayoutBinding
import com.toptal.calories.ui.admin.AdminActivity
import com.toptal.calories.ui.main.add_entry.AddEntryFragment
import com.toptal.calories.ui.main.entries_list.EntriesAdapter
import com.toptal.calories.utils.Constants
import com.toptal.calories.utils.base.BaseFragment
import com.toptal.calories.utils.getViewVisibility
import com.toptal.calories.utils.showSnackMessage

class AllEntriesFragment : BaseFragment() {

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
        toggleLoading(true)
        initViews()
        initListeners()
        observe()
        (requireActivity() as AdminActivity).adminViewModel.getAllEntries()
    }

    override fun observe() {
        (requireActivity() as AdminActivity).adminViewModel.allEntriesObservable.observe(viewLifecycleOwner) { list ->
            (binding.aefRv.adapter as EntriesAdapter).updateList(list)
            toggleLoading(false)
        }
        (requireActivity() as AdminActivity).adminViewModel.deleteEntryObservable.observe(viewLifecycleOwner) { result ->
            showSnackMessage(requireView(), getString(R.string.entry_deleted))
            (binding.aefRv.adapter as EntriesAdapter).removeEntry(result.first, result.second)
            toggleLoading(false)
        }
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
            val fragment = AddEntryFragment()
            val bundle = Bundle()
            bundle.putBoolean(Constants.BUNDLE_ADMIN_ADD_KEY, true)
            fragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(binding.aefFrameContainer.id, fragment)
                .addToBackStack(AddEntryFragment::class.java.simpleName).commit()
            it.visibility = getViewVisibility(false)
        }
    }

}