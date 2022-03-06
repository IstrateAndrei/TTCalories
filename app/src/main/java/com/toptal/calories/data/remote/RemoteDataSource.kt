package com.toptal.calories.data.remote

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.toptal.calories.data.model.FoodEntry
import com.toptal.calories.utils.getUpdateMap
import org.koin.core.component.KoinComponent

class RemoteDataSource : KoinComponent {

    fun getEntriesByUserId(userId: String, listener: EventListener<*>) {
        FirebaseFirestore.getInstance().collection("entries")
            .whereEqualTo("creator_id", userId)
            .addSnapshotListener(listener as EventListener<QuerySnapshot>)
    }

    fun getAllEntries(listener: EventListener<*>) {
        FirebaseFirestore.getInstance().collection("entries")
            .addSnapshotListener(listener as EventListener<QuerySnapshot>)
    }

    fun getAllUsers(listener: EventListener<*>) {
        FirebaseFirestore.getInstance().collection("users")
            .addSnapshotListener(listener as EventListener<QuerySnapshot>)
    }

    fun getUserRole(userId: String): Task<DocumentSnapshot> {
        return FirebaseFirestore.getInstance().collection("users").document(userId).get()
    }

    fun addNewEntry(item: FoodEntry): Task<Void> {
        val entryRef = FirebaseFirestore.getInstance().collection("entries").document()
        item.entry_id = entryRef.id
        return entryRef.set(item)
    }

    fun updateEntry(item: FoodEntry): Task<Void> {
        return FirebaseFirestore.getInstance().collection("entries").document(item.entry_id)
            .update(
                getUpdateMap(item)
            )
    }

    fun deleteEntry(item: FoodEntry): Task<Void> {
        return FirebaseFirestore.getInstance().collection("entries").document(item.entry_id)
            .delete()
    }

}