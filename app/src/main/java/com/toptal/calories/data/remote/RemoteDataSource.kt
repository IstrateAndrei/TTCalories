package com.toptal.calories.data.remote

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import org.koin.core.component.KoinComponent

class RemoteDataSource : KoinComponent {

    fun getEntriesByUserId(userId: String): Task<QuerySnapshot> {
        return FirebaseFirestore.getInstance().collection("food_entries")
            .whereEqualTo("creator_id", userId).get()
    }

    fun getAllEntries(): Task<QuerySnapshot> {
        return FirebaseFirestore.getInstance().collection("food_entries").get()
    }

    fun getUserRole(userId: String): Task<DocumentSnapshot> {
        return FirebaseFirestore.getInstance().collection("users").document(userId).get()
    }

}