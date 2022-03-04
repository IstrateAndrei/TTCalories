package com.toptal.calories.ui.main.add_entry

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.toptal.calories.R
import com.toptal.calories.data.model.FoodEntry
import com.toptal.calories.databinding.FragmentAddEntryBinding
import com.toptal.calories.utils.base.BaseFragment
import com.toptal.calories.utils.showSnackMessage
import java.util.*


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AddEntryFragment : BaseFragment() {

    private var _binding: FragmentAddEntryBinding? = null
    private val binding get() = _binding!!
    private var date = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddEntryBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initViews()
        initListeners()
        observe()
    }

    override fun observe() {

    }

    override fun toggleLoading(isLoading: Boolean) {

    }

    override fun initViews() {

    }

    override fun initListeners() {
        binding.faeTimeTil.setOnClickListener {
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
            val date = binding.faeTimeEt.text.toString()

            if (foodName.isEmpty() || caloricValue.isEmpty() || date.isEmpty()) {
                showSnackMessage(requireView(), getString(R.string.complete_fields_warning_string))
                return@setOnClickListener
            } else {

                val db = FirebaseFirestore.getInstance()
                val foodEntryRef = db.collection("food_entries").document()

                val entry = FoodEntry()
                entry.timestamp = Date() //todo get data from date val
                entry.calories = caloricValue.toInt()
                entry.foodName = foodName
                entry.creator_id = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                entry.entry_id = foodEntryRef.id

                foodEntryRef.set(entry).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showSnackMessage(requireView(), "Success")
                        findNavController().popBackStack()
                    } else {
                        showSnackMessage(requireView(), "Failure")
                        return@addOnCompleteListener
                    }
                }

            }
        }


    }

    private val dateListener =
        DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            date = "$year/$month/$dayOfMonth"
            TimePickerDialog(
                requireContext(),
                timeListener,
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                true
            ).show()
        }

    private val timeListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        val displayDate =
            "$date - ${if (hourOfDay < 10) "0$hourOfDay" else "$hourOfDay"}:${if (minute < 10) "0$minute" else "$minute"}"
        binding.faeTimeEt.setText(displayDate)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}