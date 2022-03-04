package com.toptal.calories.ui.admin.entries

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.toptal.calories.data.model.FoodEntry
import org.koin.core.component.KoinComponent

class AllEntriesViewModel : ViewModel(), KoinComponent {

    var allEntriesObservable = MutableLiveData<MutableList<FoodEntry>>()

    fun getAllEntries() {
        FirebaseFirestore.getInstance().collection("food_entries").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val query = task.result
                    allEntriesObservable.value = query.toObjects(FoodEntry::class.java)
                }
            }
    }

}