package com.moneytree.light.ui.accountlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneytree.light.Status
import com.moneytree.light.data.Account
import com.moneytree.light.data.Accounts
import com.moneytree.light.data.FetchDataCallback
import com.moneytree.light.data.Repository
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {
    private val TAG = DashboardViewModel::class.java.simpleName

    var accountsByInstitution: List<Pair<String, List<Account>>> = listOf()
    var accounts: List<Account> = listOf()

    val responseStatus = MutableLiveData(Status.IDLE)

    private val _openAccountDetailEvent = MutableLiveData<Int>()
    val openAccountDetailEvent: LiveData<Int> = _openAccountDetailEvent

    private var mRepository: Repository = Repository()

    init {
        this.mRepository = Repository()

        viewModelScope.launch {
            getAccounts()
        }
    }

    private suspend fun getAccounts() {
        responseStatus.value = Status.LOADING

        viewModelScope.launch {
            mRepository.getAccounts(object : FetchDataCallback<Accounts> {
                override fun onFailure() {
                    responseStatus.postValue(Status.ERROR)
                }

                override fun onSuccess(data: Accounts) {
                    Log.d(TAG, data.toString())

                    /* save as list */
                    accounts = data.accounts
                    /* sort by name, group by institution name */
                    accountsByInstitution =
                        data.accounts.sortedBy { it.name }.groupBy { it.institution }.toList()
                    responseStatus.postValue(Status.SUCCESS)
                }
            })
        }
    }

    internal fun openAccountDetail(id: Int) {
        _openAccountDetailEvent.value = id
    }
}
