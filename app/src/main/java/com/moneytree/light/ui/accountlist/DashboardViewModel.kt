package com.moneytree.light.ui.accountlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneytree.light.Status
import com.moneytree.light.data.Account
import com.moneytree.light.data.Repository
import com.moneytree.light.data.Result
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: Repository
) : ViewModel() {
    private val TAG = DashboardViewModel::class.java.simpleName

    var accountsByInstitution: List<Pair<String, List<Account>>> = listOf()
    var accounts: List<Account> = listOf()

    val responseStatus = MutableLiveData(Status.IDLE)

    private val _openAccountDetailEvent = MutableLiveData<Account>()
    val openAccountDetailEvent: LiveData<Account> = _openAccountDetailEvent


    init {
        viewModelScope.launch {
            getAccounts(true)
        }
    }

    fun getAccounts(force: Boolean) {
        responseStatus.value = Status.LOADING

        viewModelScope.launch {
            val result = repository.getAccounts(force)

            if (result is Result.Success) {
                Log.d(TAG, result.data.toString())

                /* save as list */
                accounts = result.data.accounts
                /* sort by name, group by institution name */
                accountsByInstitution =
                    result.data.accounts.sortedBy { it.name }.groupBy { it.institution }.toList()
                responseStatus.postValue(Status.SUCCESS)
            } else {
                responseStatus.postValue(Status.ERROR)
                accounts = emptyList()
                accountsByInstitution = emptyList()
            }
        }
    }

    internal fun openAccountDetail(account: Account) {
        _openAccountDetailEvent.value = account
    }
}
