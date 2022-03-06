package com.toptal.calories.ui.auth.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.toptal.calories.data.model.User
import org.koin.core.component.KoinComponent

class RegisterViewModel : ViewModel(), KoinComponent {

    val registerObservable = MutableLiveData<Task<AuthResult>>()
    val sendVerificationObservable = MutableLiveData<Task<Void>>()

    fun registerAuthUser(email: String, password: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                registerObservable.value = task
            }
    }

    fun sendVerificationEmail() {
        FirebaseAuth.getInstance().currentUser?.sendEmailVerification()?.addOnCompleteListener {
            sendVerificationObservable.value = it
        }
    }

    fun updateUserName(username: String) {
        FirebaseAuth.getInstance().currentUser?.let {
            val usernameUpdate = UserProfileChangeRequest.Builder().setDisplayName(username).build()
            it.updateProfile(usernameUpdate)
        }
    }

    fun registerToFireStore(email: String, username: String, uid: String) {
        val newUser = User()
        newUser.email = email
        newUser.admin = false
        newUser.name = username
        newUser.user_id = uid

        FirebaseFirestore.getInstance().collection("users").document().set(newUser).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.v("Firestore", "New user added successfully!")
            }
        }
    }

}