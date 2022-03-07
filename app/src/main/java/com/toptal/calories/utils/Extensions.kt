package com.toptal.calories.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.orhanobut.hawk.Hawk
import com.toptal.calories.data.model.FoodEntry
import com.toptal.calories.ui.MainActivity
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap


fun Activity.closeKeyboard(view: View?) {
    val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE)
    view?.let {
        (inputManager as InputMethodManager).hideSoftInputFromWindow(it.windowToken, 0)
    }
}


fun matchesEmailPattern(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun getViewVisibility(isVisible: Boolean) = if (isVisible) View.VISIBLE else View.GONE

fun showSnackMessage(view: View, message: String) {
    Snackbar.make(
        view,
        message,
        Snackbar.LENGTH_SHORT
    ).show()
}

fun isUserLoggedIn(): Boolean {
    return FirebaseAuth.getInstance().currentUser != null
}

fun Activity.openMainScreen() {
    startActivity(Intent(this, MainActivity::class.java))
    this.finish()
}

fun setAdminRights(isAdmin: Boolean) {
    Hawk.put(Constants.HAWK_HAS_ADMIN_RIGHTS_KEY, isAdmin)
}

fun hasAdminRights(): Boolean {
    return if (Hawk.get<Boolean>(Constants.HAWK_HAS_ADMIN_RIGHTS_KEY) == null) false
    else {
        Hawk.get(Constants.HAWK_HAS_ADMIN_RIGHTS_KEY)
    }
}

fun getCurrentUserId(): String {
    return FirebaseAuth.getInstance().currentUser?.uid ?: ""
}

fun getStringFromDate(input: Date): String {
    val pattern = "MMM d, yyyy"
    val dateFormat = SimpleDateFormat(pattern)
    return dateFormat.format(input)
}

fun getDateFromString(input: String): Date {
    val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
    val localDate = LocalDate.parse(input, formatter)
    return Date.from(localDate.atStartOfDay().toInstant(ZoneOffset.UTC))
}

@SuppressLint("SimpleDateFormat")
fun getStringFromDateTime(input: Date): String {
    val pattern = "MMM d, yyyy HH:mm"
    val dateFormat = SimpleDateFormat(pattern)
    return dateFormat.format(input)
}

fun getDateTimeFromString(input: String): Date {
    val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy HH:mm")
    val localDate = LocalDateTime.parse(input, formatter)
    return Date.from(localDate.toInstant(ZoneOffset.UTC))
}

fun getUpdateMap(item: FoodEntry): HashMap<String, Any> {
    val map = HashMap<String, Any>()
    map["name"] = item.name!!
    map["calories"] = item.calories
    map["entry_date"] = item.entry_date!!

    return map
}

fun getDaysAgoDate(ago: Int): Date {
    val millis = Calendar.getInstance().timeInMillis - ago * 24 * 60 * 60 * 1000
    return Date(millis)
}

//Filter dialog extensions
//From
fun setFromFilter(date: Date) {
    Hawk.put(Constants.HAWK_FROM_FILTER_KEY, date)
}

fun hasFromFilter(): Boolean {
    return Hawk.get<Date>(Constants.HAWK_FROM_FILTER_KEY) != null
}

fun getFromFilter(): Date? {
    return if (Hawk.get<Date>(Constants.HAWK_FROM_FILTER_KEY) != null) Hawk.get(Constants.HAWK_FROM_FILTER_KEY)
    else null
}

fun clearFromFilter() {
    Hawk.delete(Constants.HAWK_FROM_FILTER_KEY)
}

//To
fun setToFilter(date: Date) {
    Hawk.put(Constants.HAWK_TO_FILTER_KEY, date)
}

fun hasToFilter(): Boolean {
    return Hawk.get<Date>(Constants.HAWK_TO_FILTER_KEY) != null
}

fun getToFilter(): Date? {
    return if (Hawk.get<Date>(Constants.HAWK_TO_FILTER_KEY) != null) Hawk.get(Constants.HAWK_TO_FILTER_KEY)
    else null
}

fun clearToFilter() {
    Hawk.delete(Constants.HAWK_TO_FILTER_KEY)
}

@SuppressLint("ObsoleteSdkInt")
fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            //for check internet over Bluetooth
//            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    } else {
        return connectivityManager.activeNetworkInfo?.isConnected ?: false
    }
}

