package com.toptal.calories.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.toptal.calories.data.model.FoodEntry
import com.toptal.calories.data.remote.RemoteDataSource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Repository : KoinComponent {

    private val remoteDataSource by inject<RemoteDataSource>()


    fun getAllEntries(listener: EventListener<*>) {
        remoteDataSource.getAllEntries(listener)
    }

    fun getUserRole(userId: String): Task<DocumentSnapshot> {
        return remoteDataSource.getUserRole(userId)
    }

    fun getAllUsers(listener: EventListener<*>){
        remoteDataSource.getAllUsers(listener)
    }

    fun getUserEntries(userId: String, listener: EventListener<*>) {
        remoteDataSource.getEntriesByUserId(userId, listener)
    }

    fun addNewEntry(item: FoodEntry): Task<Void> {
        return remoteDataSource.addNewEntry(item)
    }

    fun updateEntry(item: FoodEntry): Task<Void> {
        return remoteDataSource.updateEntry(item)
    }

    fun deleteEntry(item: FoodEntry): Task<Void> {
        return remoteDataSource.deleteEntry(item)
    }

}