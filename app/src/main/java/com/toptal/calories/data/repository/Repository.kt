package com.toptal.calories.data.repository

import androidx.lifecycle.LiveData
import com.toptal.calories.data.model.FoodEntry
import java.util.*

class Repository {


    fun getEntries(): MutableList<FoodEntry> {
        //todo this remote - Realtime DB Firebase

        //mocked data for now
        var list = mutableListOf<FoodEntry>()

        for (i in 0..10) {
            val entry = FoodEntry()
            entry.timeStamp = Date()
            entry.foodName = "Food ${i + 1}"
            entry.calories = (224 * i).toFloat()
            list.add(entry)
        }

        return list
    }


}