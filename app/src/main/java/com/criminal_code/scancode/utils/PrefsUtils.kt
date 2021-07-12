package com.criminal_code.scancode.utils

import android.content.Context
import com.criminal_code.scancode.app.MyApp

private val prefs by lazy {
    MyApp.context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
}

fun getNotesSortMethodName(defaultValue: String): String = prefs.getString("sort_method", defaultValue).toString()

fun setNotesSortMethod(sortMethod: String) {
    prefs.edit().putString("sort_method", sortMethod).apply()
}