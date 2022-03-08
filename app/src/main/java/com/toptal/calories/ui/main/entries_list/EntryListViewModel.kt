package com.toptal.calories.ui.main.entries_list

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.toptal.calories.data.model.FoodEntry
import com.toptal.calories.data.repository.Repository
import com.toptal.calories.utils.getFromFilter
import com.toptal.calories.utils.getToFilter
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.lang.Exception
import java.util.*

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