package com.toptal.calories.ui.admin.entries

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.toptal.calories.data.model.FoodEntry
import com.toptal.calories.data.model.User
import com.toptal.calories.data.repository.Repository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AdminViewModel : ViewModel(), KoinComponent {

    private val repository by inject<Repository>()

    var entriesObservable = MutableLiveData<MutableList<FoodEntry>>()
    var deleteEntryObservable = MutableLiveData<Pair<FoodEntry, Int>>()
    val firstWeekObservable = MutableLiveData<MutableList<FoodEntry>>()
    val errorObservable = MutableLiveData<Exception>()

    fun getAdminEntries() {
        val snapShotListener = EventListener<QuerySnapshot> { value, error ->
            if (error != null) {
                errorObservable.value = error
                return@EventListener
            }
            entriesObservable.value = value?.toObjects(FoodEntry::class.java)
        }
        repository.getAdminEntries(snapShotListener)
    }

    fun getFirstWeekEntries() {
        val snapShotListener = EventListener<QuerySnapshot> { value, error ->
            if (error != null) {
                errorObservable.value = error
                return@EventListener
            }
            firstWeekObservable.value = value?.toObjects(FoodEntry::class.java)
        }
        repository.getFirstWeekEntries(snapShotListener)
    }

    fun deleteEntry(item: FoodEntry, position: Int) {
        repository.deleteEntry(item).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                deleteEntryObservable.value = Pair(item, position)
            }
        }
    }
}