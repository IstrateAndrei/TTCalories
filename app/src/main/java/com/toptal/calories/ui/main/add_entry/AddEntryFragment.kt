package com.toptal.calories.ui.main.add_entry

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.toptal.calories.R
import com.toptal.calories.data.model.FoodEntry
import com.toptal.calories.databinding.AddEntryFragmentLayoutBinding
import com.toptal.calories.ui.admin.AdminActivity

import com.toptal.calories.utils.*
import com.toptal.calories.utils.base.BaseFragment
import java.util.*

class AddEntryFragment : BaseFragment() {

    private var _binding: AddEntryFragmentLayoutBinding? = null
    private val binding get() = _binding!!

    private var selectedDate: Calendar? = null

    private lateinit var addEntryViewModel: AddEntryViewModel

    //admin panel edit
    private var isAdminAdd = false
    private var adminItem: FoodEntry? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddEntryFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addEntryViewModel = ViewModelProvider(this).get(AddEntryViewModel::class.java)
        arguments?.let {
            isAdminAdd = it.getBoolean(Constants.BUNDLE_ADMIN_ADD_KEY)
            adminItem = it.getParcelable(Constants.BUNDLE_ADMIN_ITEM_KEY)
        }
        initViews()
        initListeners()
        observe()
    }

    override fun observe() {
        addEntryViewModel.addNewEntryObservable.observe(viewLifecycleOwner) {
            showSnackMessage(requireView(), "Added new food entry!")
            if (isAdminAdd) {
                requireActivity().supportFragmentManager.popBackStack()
//                (requireActivity() as AdminActivity).adminViewModel.getFirstWeekEntries()
            } else {
                findNavController().popBackStack()
            }
        }
        addEntryViewModel.updateEntryObservable.observe(viewLifecycleOwner) {
            showSnackMessage(requireView(), "Food entry updated!")
            requireActivity().supportFragmentManager.popBackStack()
//            (requireActivity() as AdminActivity).adminViewModel.getFirstWeekEntries()
        }
    }

    override fun toggleLoading(isLoading: Boolean) {
        //no progress bar here...screen pops after adding/updating
    }

    override fun initViews() {
        if (adminItem != null) {
            //show delete button
            adminItem?.let { entry ->
                binding.faeAddBtn.text = requireActivity().resources.getString(R.string.update)
                entry.name?.let {
                    binding.faeFoodEt.setText(it)
                }
                binding.faeCalorieEt.setText(entry.calories.toString())
                entry.entry_date?.let {
                    binding.faeDateTv.visibility = getViewVisibility(true)
                    binding.faeDateTv.text = getStringFromDateTime(it)
                }
            }
        } else {
            //normal add entry flow
        }
    }

    override fun initListeners() {

        binding.faePickDateBtn.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                dateListener,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.faeAddBtn.setOnClickListener {
            val foodName = binding.faeFoodEt.text.toString()
            val caloricValue = binding.faeCalorieEt.text.toString()
            val entryDate = binding.faeDateTv.text.toString()

            if (foodName.isEmpty() || caloricValue.isEmpty() || entryDate.isEmpty()) {
                showSnackMessage(requireView(), getString(R.string.complete_fields_warning_string))
                return@setOnClickListener
            }

            if (adminItem != null) {
                adminItem?.let { item ->
                    if (item.name == foodName && item.calories.toString() == caloricValue && getStringFromDateTime(
                            item.entry_date!!
                        ) == entryDate
                    ) {
                        showSnackMessage(requireView(), "Provide a change before editing!")
                        return@setOnClickListener
                    } else {
                        val updatedItem = FoodEntry()
                        updatedItem.name = foodName
                        updatedItem.calories = caloricValue.toInt()
                        updatedItem.created_at = null
                        updatedItem.creator_id = item.creator_id
                        updatedItem.entry_id = item.entry_id
                        updatedItem.entry_date =
                            if (selectedDate != null) selectedDate!!.time else getDateTimeFromString(
                                entryDate
                            )
                        addEntryViewModel.updateEntry(updatedItem)
                    }
                }
            } else {

                val entry = FoodEntry()
                entry.created_at = null
                entry.calories = caloricValue.toInt()
                entry.name = foodName
                entry.entry_date = selectedDate!!.time
                entry.creator_id = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                addEntryViewModel.addNewEntry(entry)
            }
        }
    }

    private val dateListener =
        DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            selectedDate = Calendar.getInstance()
            selectedDate?.set(year, month, dayOfMonth)
            TimePickerDialog(
                requireContext(),
                timeListener,
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                true
            ).show()
        }

    private val timeListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->

        selectedDate?.set(Calendar.HOUR_OF_DAY, hourOfDay)
        selectedDate?.set(Calendar.MINUTE, minute)

        binding.faeDateTv.visibility = getViewVisibility(true)
        binding.faeDateTv.text = selectedDate?.let { getStringFromDateTime(it.time) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}