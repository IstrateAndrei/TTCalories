package com.toptal.calories.data.model


import android.os.Parcelable
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.*

@IgnoreExtraProperties
@Parcelize
data class FoodEntry(
    @ServerTimestamp
    var created_at: Date? = null,
    @ServerTimestamp
    var entry_date: Date? = null,
    var name: String? = null,
    var calories: Int = 0,
    var creator_id: String = "",
    var entry_id: String = ""
) : Parcelable
