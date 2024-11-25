package com.gmwapp.dostt.retrofit.callbacks

import retrofit2.Callback

interface NetworkCallback<T> : Callback<T> {
    fun onNoNetwork()
}