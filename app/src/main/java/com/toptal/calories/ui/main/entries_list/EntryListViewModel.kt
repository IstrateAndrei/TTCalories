package com.toptal.calories.ui.main.entries_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.toptal.calories.data.model.FoodEntry
import com.toptal.calories.data.repository.Repository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EntryListViewModel : ViewModel(), KoinComponent {

    private val repository by inject<Repository>()

    var getFoodEntriesObservable = MutableLiveData<MutableList<FoodEntry>>()
    var errorObservable = MutableLiveData<Exception>()


    fun getUserEntries(userId: String) {
        val snapShotListener = EventListener<QuerySnapshot> { value, error ->
            if (error != null) {
                errorObservable.value = error
                return@EventListener
            }
            getFoodEntriesObservable.value = value?.toObjects(FoodEntry::class.java)
        }
        repository.getUserEntries(userId, snapShotListener)
    }

}