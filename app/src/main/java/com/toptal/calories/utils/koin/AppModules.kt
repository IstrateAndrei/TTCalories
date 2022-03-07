package com.toptal.calories.utils.koin

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.toptal.calories.data.local.LocalDataSource
import com.toptal.calories.data.remote.RemoteDataSource
import com.toptal.calories.data.repository.Repository
import org.koin.core.module.Module
import org.koin.dsl.module


object AppModules {

    private val remoteDataSourceModule: Module = module {
        single { RemoteDataSource() }
    }

    private val localDataSourceModule: Module = module {
        single { LocalDataSource() }
    }

    private val repoModule: Module = module {
        single { Repository() }
    }

    private val fireStoreModule: Module = module {
        single {
            val db = FirebaseFirestore.getInstance()
            val settings = firestoreSettings {
                isPersistenceEnabled = true
            }
            db.firestoreSettings = settings
            return@single db
        }
    }

    val appModules =
        listOf(repoModule, remoteDataSourceModule, localDataSourceModule, fireStoreModule)

}