package com.toptal.calories.ui.admin.entries

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.toptal.calories.data.model.FoodEntry
import com.toptal.calories.data.model.User
import com.toptal.calories.data.repository.Repository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AllEntriesViewModel : ViewModel(), KoinComponent {

    private val repository by inject<Repository>()

    var allEntriesObservable = MutableLiveData<MutableList<FoodEntry>>()
    var deleteEntryObservable = MutableLiveData<Pair<FoodEntry, Int>>()

    val allUsersObservable = MutableLiveData<MutableList<User>>()

    fun getAllEntries() {
        val snapShotListener = EventListener<QuerySnapshot> { value, error ->
            if (error != null) {
                //fail
                Log.e("Fail", "Failure")
                return@EventListener
            }
            allEntriesObservable.value = value?.toObjects(FoodEntry::class.java)
        }
        repository.getAllEntries(snapShotListener)
    }

    fun deleteEntry(item: FoodEntry, position: Int) {
        repository.deleteEntry(item).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                deleteEntryObservable.value = Pair(item, position)
            }
        }
    }

    fun getAllUsers() {
        val snapShotListener = EventListener<QuerySnapshot> { value, error ->
            if (error != null) {
                //fail
                Log.e("Fail", "Failure")
                return@EventListener
            }
            allUsersObservable.value = value?.toObjects(User::class.java)
        }
        repository.getAllUsers(snapShotListener)
    }

}