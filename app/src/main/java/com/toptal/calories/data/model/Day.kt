package com.toptal.calories.data.model

data class Day(
    var dateString: String = "",
    var entriesList: MutableList<FoodEntry> = mutableListOf()
)
