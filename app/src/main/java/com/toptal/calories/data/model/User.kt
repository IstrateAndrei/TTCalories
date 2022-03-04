package com.toptal.calories.data.model

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var name: String = "",
    var user_id: String = "",
    var admin: Boolean = false
)
