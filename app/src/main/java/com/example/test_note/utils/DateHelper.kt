package com.example.test_note.utils

import android.util.Log
import com.example.test_note.ui.activity.ERROR_TAG
import java.text.SimpleDateFormat
import java.util.*

class DateHelper {

    fun getStringFromDate(timeInMillis: Long): String {
        return try {
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            val calendar: Calendar = Calendar.getInstance()
            calendar.timeInMillis = timeInMillis
            formatter.format(calendar.time)
        } catch (e: Exception) {
            Log.e(ERROR_TAG, e.toString())
            ""
        }
    }
}