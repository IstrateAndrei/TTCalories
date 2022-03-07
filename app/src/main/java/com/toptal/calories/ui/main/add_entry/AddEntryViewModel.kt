package com.toptal.calories.ui.main.add_entry

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.toptal.calories.data.model.FoodEntry
import com.toptal.calories.data.repository.Repository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AddEntryViewModel : ViewModel(), KoinComponent {

    private val repository by inject<Repository>()

    val addNewEntryObservable = MutableLiveData<Void>()
    val updateEntryObservable = MutableLiveData<Void>()

    fun addNewEntry(item: FoodEntry) {
        repository.addNewEntry(item).addOnCompleteListener { task ->
            addNewEntryObservable.value = task.result
        }
    }

    fun updateEntry(item: FoodEntry) {
        repository.updateEntry(item).addOnCompleteListener { task ->
            updateEntryObservable.value = task.result
        }
    }
}