package com.gmwapp.hima.utils

import android.content.Context
import android.content.SharedPreferences
import com.gmwapp.hima.retrofit.responses.UserData
import com.google.gson.Gson


class DPreferences(context: Context) {
    private val mPrefsRead: SharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    private val mPrefsWrite: SharedPreferences.Editor = mPrefsRead.edit()

    fun setUserData(userData: UserData) {
        mPrefsWrite.putString(
            USER_DATA,
            Gson().toJson(userData)
        )
        mPrefsWrite.apply()
    }

    fun getUserData():UserData? {
        try {
            return Gson().fromJson(mPrefsRead.getString(USER_DATA,""), UserData::class.java);
        } catch (e: Exception) {
            return null;
        }
    }

    companion object {
        private const val REGISTERED = "registered"
        private const val USER_DATA = "user_data"
        private const val PREFS = "Hima"
    }
}