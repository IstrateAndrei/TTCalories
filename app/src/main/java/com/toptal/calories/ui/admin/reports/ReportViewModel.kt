package com.toptal.calories.ui.admin.reports

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

class ReportViewModel : ViewModel(), KoinComponent {

    val firstWeekObservable = MutableLiveData<MutableList<FoodEntry>>()
    val otherWeekObservable = MutableLiveData<MutableList<FoodEntry>>()
    val allUsersObservable = MutableLiveData<MutableList<User>>()
    val errorObservable = MutableLiveData<Exception>()

    val repository by inject<Repository>()

    fun getFirstWeekEntries() {
        val snapShotListener = EventListener<QuerySnapshot> { value, error ->
            if (error != null) {
                Log.e("Fail", "Failure")
                errorObservable.value = error
                return@EventListener
            }
            firstWeekObservable.value = value?.toObjects(FoodEntry::class.java)
        }
        repository.getFirstWeekEntries(snapShotListener)
    }

    fun getSecondWeekEntries() {
        val snapShotListener = EventListener<QuerySnapshot> { value, error ->
            if (error != null) {
                Log.e("Fail", "Failure")
                errorObservable.value = error
                return@EventListener
            }
            otherWeekObservable.value = value?.toObjects(FoodEntry::class.java)
        }
        repository.getSecondWeekEntries(snapShotListener)
    }

    fun getAllUsers() {
        val snapShotListener = EventListener<QuerySnapshot> { value, error ->
            if (error != null) {
                //fail
                Log.e("Fail", "Failure")
                errorObservable.value = error
                return@EventListener
            }
            allUsersObservable.value = value?.toObjects(User::class.java)
        }
        repository.getAllUsers(snapShotListener)
    }
}