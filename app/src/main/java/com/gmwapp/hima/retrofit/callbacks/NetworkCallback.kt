package com.gmwapp.hima.retrofit.callbacks

import retrofit2.Callback

interface NetworkCallback<T> : Callback<T> {
    fun onNoNetwork()
}