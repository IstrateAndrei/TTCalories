package com.toptal.calories.ui.auth.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import org.koin.core.component.KoinComponent

class LoginViewModel : ViewModel(), KoinComponent {


    val loginObservable = MutableLiveData<Task<AuthResult>>()

    fun loginAction(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            loginObservable.value = task
        }
    }

    fun loginWithToken(token: String) {
        FirebaseAuth.getInstance().signInWithCustomToken(token).addOnCompleteListener { task ->
            loginObservable.value = task
        }
    }
}