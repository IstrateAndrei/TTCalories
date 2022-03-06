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
import com.toptal.calories.databinding.EntryListFragmentLayoutBinding
import com.toptal.calories.utils.base.BaseFragment
import com.toptal.calories.utils.getStringFromDate
import com.toptal.calories.utils.getViewVisibility
import java.util.*

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
        mViewModel?.getFoodEntriesObservable?.observe(viewLifecycleOwner) {
            (binding.felRv.adapter as EntriesAdapter).updateList(it)
            toggleLoading(false)
        }
    }

    override fun toggleLoading(isLoading: Boolean) {
        binding.felProgress.visibility = getViewVisibility(isLoading)
    }

    override fun initViews() {
        initRecycler()
    }

    val dateListener =
        DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

            if (isFromPicked) {
                fromSelectDate = Calendar.getInstance()
                fromSelectDate?.set(year, month, dayOfMonth)
                fromSelectDate?.time?.let {
                    val display = "From:${getStringFromDate(it)}"
                    binding.felFilterFromTv.text = display
                }
                //apply filters from
                (binding.felRv.adapter as EntriesAdapter).applyFromFilter(fromSelectDate?.time)
            }

            if (isToPicked) {
                toSelectDate = Calendar.getInstance()
                toSelectDate?.set(year, month, dayOfMonth)
                toSelectDate?.time?.let {
                    val display = "To:${getStringFromDate(it)}"
                    binding.felFilterToTv.text = display
                }
                //apply filters to

            }
        }

    override fun initListeners() {
        binding.felFab.setOnClickListener {
            findNavController().navigate(R.id.entries_to_add_action)
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
    }


    fun initRecycler() {
        if (binding.felRv.layoutManager == null) binding.felRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        if (binding.felRv.adapter == null) binding.felRv.adapter = EntriesAdapter()
    }

    override fun onResume() {
        super.onResume()
        mViewModel?.getUserEntries(FirebaseAuth.getInstance().currentUser?.uid!!)
    }
}