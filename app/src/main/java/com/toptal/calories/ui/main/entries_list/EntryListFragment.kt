package com.toptal.calories.ui.main.entries_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
        toggleLoading(true)
        setHasOptionsMenu(true)
        mViewModel = ViewModelProvider(this).get(EntryListViewModel::class.java)
        initViews()
        initListeners()
        observe()
        mViewModel!!.getUserEntries(FirebaseAuth.getInstance().currentUser?.uid!!)
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
            toggleLoading(false)
        }
    }

    override fun toggleLoading(isLoading: Boolean) {

    }

    override fun initViews() {
        initRecycler()
    }

    override fun initListeners() {
        binding.felFab.setOnClickListener {
            findNavController().navigate(R.id.entries_to_add_action)
        }
    }

    fun initRecycler() {
        if (binding.felRv.layoutManager == null) binding.felRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        if (binding.felRv.adapter == null) binding.felRv.adapter = EntriesAdapter()
    }

}