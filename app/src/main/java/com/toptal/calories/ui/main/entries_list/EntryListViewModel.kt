package com.toptal.calories.ui.main.entries_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.toptal.calories.data.model.FoodEntry
import com.toptal.calories.data.repository.Repository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EntryListViewModel : ViewModel(), KoinComponent {

    private val repository by inject<Repository>()

    var getFoodEntriesObservable = MutableLiveData<MutableList<FoodEntry>>()

    fun getUserEntries(userId: String) {
        repository.getUserEntries(userId).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val q = task.result
                getFoodEntriesObservable.value = q.toObjects(FoodEntry::class.java)
            }
        }
    }

}