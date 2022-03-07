package com.toptal.calories.data.remote

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.toptal.calories.data.model.FoodEntry
import com.toptal.calories.utils.getDaysAgoDate
import com.toptal.calories.utils.getFromFilter
import com.toptal.calories.utils.getToFilter
import com.toptal.calories.utils.getUpdateMap
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class RemoteDataSource : KoinComponent {

    val fireStore by inject<FirebaseFirestore>()

    fun getEntriesByUserId(userId: String, listener: EventListener<*>) {
        fireStore.collection("entries")
            .whereEqualTo("creator_id", userId)
            .whereGreaterThan("entry_date", getFromFilter() ?: getDaysAgoDate(20))
            .whereLessThan("entry_date", getToFilter() ?: Date())
            .addSnapshotListener(listener as EventListener<QuerySnapshot>)
    }

    fun getAdminEntries(listener: EventListener<*>) {
        fireStore.collection("entries")
            .whereGreaterThan("entry_date", getFromFilter() ?: getDaysAgoDate(20))
            .whereLessThan("entry_date", getToFilter() ?: Date())
            .addSnapshotListener(listener as EventListener<QuerySnapshot>)
    }

    fun getFirstWeekEntries(listener: EventListener<*>) {
        fireStore.collection("entries")
            .whereGreaterThan("entry_date", getDaysAgoDate(7))
            .addSnapshotListener(listener as EventListener<QuerySnapshot>)
    }

    fun getSecondWeekEntries(listener: EventListener<*>) {
        fireStore.collection("entries")
            .whereGreaterThan("entry_date", getDaysAgoDate(14))
            .whereLessThan("entry_date", getDaysAgoDate(7))
            .addSnapshotListener(listener as EventListener<QuerySnapshot>)
    }

    fun getAllUsers(listener: EventListener<*>) {
        fireStore.collection("users")
            .addSnapshotListener(listener as EventListener<QuerySnapshot>)
    }

    fun getUserRole(userId: String): Task<DocumentSnapshot> {
        return fireStore.collection("users").document(userId).get()
    }

    fun addNewEntry(item: FoodEntry): Task<Void> {
        val entryRef = FirebaseFirestore.getInstance().collection("entries").document()
        item.entry_id = entryRef.id
        return entryRef.set(item)
    }

    fun updateEntry(item: FoodEntry): Task<Void> {
        return fireStore.collection("entries").document(item.entry_id)
            .update(
                getUpdateMap(item)
            )
    }

    fun deleteEntry(item: FoodEntry): Task<Void> {
        return fireStore.collection("entries").document(item.entry_id)
            .delete()
    }

}