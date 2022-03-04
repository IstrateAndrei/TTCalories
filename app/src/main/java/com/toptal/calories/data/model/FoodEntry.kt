package com.toptal.calories.data.model

import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class FoodEntry(
    var timestamp: Date? = null,
    var foodName: String? = null,
    var calories: Int = 0,
    var creator_id: String = "",
    var entry_id: String = ""
)
