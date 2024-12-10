package com.gmwapp.hima.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmwapp.hima.constants.DConstants
import com.gmwapp.hima.repositories.FemaleUsersRepositories
import com.gmwapp.hima.repositories.TransactionsRepositories
import com.gmwapp.hima.retrofit.callbacks.NetworkCallback
import com.gmwapp.hima.retrofit.responses.FemaleUsersResponse
import com.gmwapp.hima.retrofit.responses.TransactionsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class FemaleUsersViewModel @Inject constructor(private val femaleUsersRepositories: FemaleUsersRepositories) : ViewModel() {

    val femaleUsersResponseLiveData = MutableLiveData<FemaleUsersResponse>()
    val femaleUsersErrorLiveData = MutableLiveData<String>()

    fun getFemaleUsers(userId: Int) {
        viewModelScope.launch {
            femaleUsersRepositories.getFemaleUsers(1, object:NetworkCallback<FemaleUsersResponse> {
                override fun onResponse(
                    call: Call<FemaleUsersResponse>,
                    response: Response<FemaleUsersResponse>
                ) {
                    femaleUsersResponseLiveData.postValue(response.body());
                }

                override fun onFailure(call: Call<FemaleUsersResponse>, t: Throwable) {
                    femaleUsersErrorLiveData.postValue(DConstants.LOGIN_ERROR);
                }

                override fun onNoNetwork() {
                    femaleUsersErrorLiveData.postValue(DConstants.NO_NETWORK);
                }
            })
        }
    }

}

