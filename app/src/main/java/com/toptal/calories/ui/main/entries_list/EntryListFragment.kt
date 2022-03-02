package com.toptal.calories.ui.main.entries_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.toptal.calories.R
import com.toptal.calories.databinding.FragmentEntryListBinding
import com.toptal.calories.utils.base.BaseFragment

class EntryListFragment : BaseFragment() {

    private var _binding: FragmentEntryListBinding? = null
    private var mViewModel: EntryListViewModel? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEntryListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = ViewModelProvider(this).get(EntryListViewModel::class.java)
        initViews()
        initListeners()
        observe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun observe() {
        mViewModel?.getFoodEntriesObservable?.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                (binding.felRv.adapter as EntriesAdapter).updateList(it)
            }
        }
    }

    override fun toggleLoading(isLoading: Boolean) {

    }

    override fun initViews() {
        initRecycler()
    }

    override fun initListeners() {
        binding.felFab.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    fun initRecycler() {
        if (binding.felRv.layoutManager == null) binding.felRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        if (binding.felRv.adapter == null) binding.felRv.adapter = EntriesAdapter()
    }

}