package com.gmwapp.hima.utils

import android.content.Context
import android.content.SharedPreferences
import com.gmwapp.hima.retrofit.responses.UserData
import com.google.gson.Gson


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

    fun setUserData(userData: UserData) {
        mPrefsWrite.putString(
            DPreferences.USER_DATA,
            Gson().toJson(userData)
        )
        mPrefsWrite.apply()
    }

    fun getUserData():UserData {
        return Gson().fromJson(mPrefsRead.getString(USER_DATA,""), UserData::class.java);
    }

    companion object {
        private const val REGISTERED = "registered"
        private const val USER_DATA = "user_data"
        private const val PREFS = "Hima"
    }
}