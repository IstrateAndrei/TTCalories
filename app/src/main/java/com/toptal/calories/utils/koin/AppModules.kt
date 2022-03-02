package com.toptal.calories.utils.koin

import com.toptal.calories.data.local.LocalDataSource
import com.toptal.calories.data.remote.RemoteDataSource
import com.toptal.calories.data.repository.Repository
import org.koin.core.module.Module
import org.koin.dsl.module


object AppModules {
    private val retrofitModule: Module = module {
        single {
//            val interceptor = HttpLoggingInterceptor()
//            interceptor.level = HttpLoggingInterceptor.Level.BODY
//
//            val client = OkHttpClient.Builder()
//            client.addInterceptor(interceptor)
//
//            Retrofit.Builder()
//                .baseUrl("https://lynx-workout.firebaseio.com/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .client(client.build())
//                .build()
//                .create(ApiInterface::class.java)
        }
    }

    private val remoteDataSourceModule: Module = module {
        single { RemoteDataSource() }
    }

    private val localDataSourceModule: Module = module {
        single { LocalDataSource() }
    }

    private val repoModule: Module = module {
        single { Repository() }
    }

    val appModules =
        listOf(retrofitModule, repoModule, remoteDataSourceModule, localDataSourceModule)

}