package com.gmwapp.dostt.utils

import android.content.Context
import android.content.SharedPreferences

class DPreferences(context: Context) {
    private val mPrefsRead: SharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    private val mPrefsWrite: SharedPreferences.Editor = mPrefsRead.edit()

    fun setIsRegistered(registered: Boolean) {
        mPrefsWrite.putBoolean(
            DPreferences.REGISTERED,
            registered
        )
        mPrefsWrite.apply()
    }

    var isRegistered: Boolean
        get() = mPrefsRead.getBoolean(REGISTERED, false)
        set(validUser) {
            mPrefsWrite.putBoolean(REGISTERED, validUser)
            mPrefsWrite.apply()
        }

    companion object {
        private const val REGISTERED = "registered"
        private const val PREFS = "Dostt"
    }
}