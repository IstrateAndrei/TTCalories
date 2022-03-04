package com.toptal.calories.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.toptal.calories.data.model.FoodEntry
import com.toptal.calories.data.remote.RemoteDataSource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class Repository : KoinComponent {

    private val remoteDataSource by inject<RemoteDataSource>()


    fun getAllEntries(): Task<QuerySnapshot> {
        return remoteDataSource.getAllEntries()
    }

    fun getUserRole(userId: String): Task<DocumentSnapshot> {
        return remoteDataSource.getUserRole(userId)
    }

    fun getUserEntries(userId: String): Task<QuerySnapshot> {
        return remoteDataSource.getEntriesByUserId(userId)
    }

}