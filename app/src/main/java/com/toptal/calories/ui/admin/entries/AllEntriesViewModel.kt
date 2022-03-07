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

class AllEntriesViewModel : ViewModel(), KoinComponent {

    private val repository by inject<Repository>()

    var entriesObservable = MutableLiveData<MutableList<FoodEntry>>()
    var deleteEntryObservable = MutableLiveData<Pair<FoodEntry, Int>>()
    val errorObservable = MutableLiveData<Exception>()

    fun getAdminEntries() {
        val snapShotListener = EventListener<QuerySnapshot> { value, error ->
            if (error != null) {
                Log.e("Fail", "Failure")
                errorObservable.value = error
                return@EventListener
            }
            entriesObservable.value = value?.toObjects(FoodEntry::class.java)
        }
        repository.getAdminEntries(snapShotListener)
    }

    fun deleteEntry(item: FoodEntry, position: Int) {
        repository.deleteEntry(item).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                deleteEntryObservable.value = Pair(item, position)
            }
        }
    }
}