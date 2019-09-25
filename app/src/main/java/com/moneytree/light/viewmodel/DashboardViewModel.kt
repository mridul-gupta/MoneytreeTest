package com.moneytree.light.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneytree.light.Status
import com.moneytree.light.pojo.Accounts
import com.moneytree.light.pojo.FetchDataCallback
import com.moneytree.light.pojo.Repository
import com.moneytree.light.pojo.Transactions
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {
    private val TAG = "DashboardViewModel"

    val responseStatus = MutableLiveData(Status.IDLE)

    private var mRepository: Repository? = null

    init {
        this.mRepository = Repository()

        viewModelScope.launch {
            getAccounts()
        }
    }

    private suspend fun getAccounts() {
        responseStatus.value = Status.LOADING

        viewModelScope.launch {
            mRepository?.getAccounts(object : FetchDataCallback<Accounts> {
                override fun onFailure() {
                    responseStatus.postValue(Status.ERROR)
                }

                override fun onSuccess(data: Accounts) {
                    Log.d(TAG, data.toString())
                    responseStatus.postValue(Status.SUCCESS)
                }
            })
        }
    }

    private fun getAccountData(accountNum : Int) {
        viewModelScope.launch {

            mRepository?.getAccountData(accountNum, object : FetchDataCallback<Transactions> {
                override fun onFailure() {
                    responseStatus.postValue(Status.ERROR)
                }

                override fun onSuccess(data: Transactions) {
                    Log.d(TAG, data.toString())
                    responseStatus.postValue(Status.SUCCESS)
                }
            })
        }
    }
}
