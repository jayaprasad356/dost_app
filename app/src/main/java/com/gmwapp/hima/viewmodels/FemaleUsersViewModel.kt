package com.gmwapp.hima.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmwapp.hima.constants.DConstants
import com.gmwapp.hima.repositories.FemaleUsersRepositories
import com.gmwapp.hima.repositories.TransactionsRepositories
import com.gmwapp.hima.retrofit.callbacks.NetworkCallback
import com.gmwapp.hima.retrofit.responses.FemaleUsersResponse
import com.gmwapp.hima.retrofit.responses.RandomUsersResponse
import com.gmwapp.hima.retrofit.responses.TransactionsResponse
import com.gmwapp.hima.retrofit.responses.UpdateCallStatusResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class FemaleUsersViewModel @Inject constructor(private val femaleUsersRepositories: FemaleUsersRepositories) : ViewModel() {

    val femaleUsersResponseLiveData = MutableLiveData<FemaleUsersResponse>()
    val femaleUsersErrorLiveData = MutableLiveData<String>()

    val randomUsersResponseLiveData = MutableLiveData<RandomUsersResponse>()
    val randomUsersErrorLiveData = MutableLiveData<String>()

    val updateCallStatusResponseLiveData = MutableLiveData<UpdateCallStatusResponse>()
    val updateCallStatusErrorLiveData = MutableLiveData<String>()

    fun getFemaleUsers(userId: Int) {
        viewModelScope.launch {
            femaleUsersRepositories.getFemaleUsers(userId, object:NetworkCallback<FemaleUsersResponse> {
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

    fun getRandomUser(userId: Int, callType: String) {
        viewModelScope.launch {
            femaleUsersRepositories.getRandomUser(userId,callType, object:NetworkCallback<RandomUsersResponse> {
                override fun onResponse(
                    call: Call<RandomUsersResponse>,
                    response: Response<RandomUsersResponse>
                ) {
                    randomUsersResponseLiveData.postValue(response.body());
                }

                override fun onFailure(call: Call<RandomUsersResponse>, t: Throwable) {
                    randomUsersErrorLiveData.postValue(DConstants.LOGIN_ERROR);
                }

                override fun onNoNetwork() {
                    randomUsersErrorLiveData.postValue(DConstants.NO_NETWORK);
                }
            })
        }
    }

    fun updateCallStatus(userId: Int, callType:String, status:Int) {
        viewModelScope.launch {
            femaleUsersRepositories.updateCallStatus(userId,callType,status, object:NetworkCallback<UpdateCallStatusResponse> {
                override fun onResponse(
                    call: Call<UpdateCallStatusResponse>,
                    response: Response<UpdateCallStatusResponse>
                ) {
                    updateCallStatusResponseLiveData.postValue(response.body());
                }

                override fun onFailure(call: Call<UpdateCallStatusResponse>, t: Throwable) {
                    updateCallStatusErrorLiveData.postValue(DConstants.LOGIN_ERROR);
                }

                override fun onNoNetwork() {
                    updateCallStatusErrorLiveData.postValue(DConstants.NO_NETWORK);
                }
            })
        }
    }

}

