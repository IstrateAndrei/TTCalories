package com.toptal.calories.data.model

import java.util.*

data class FoodEntry(
    var timeStamp: Date? = null,
    var foodName: String? = null,
    var calories: Float? = 0.0F
)
