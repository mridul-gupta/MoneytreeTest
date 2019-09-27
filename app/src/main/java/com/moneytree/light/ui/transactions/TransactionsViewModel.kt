package com.moneytree.light.ui.transactions

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneytree.light.Status
import com.moneytree.light.data.FetchDataCallback
import com.moneytree.light.data.Repository
import com.moneytree.light.data.Transaction
import com.moneytree.light.data.Transactions
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class TransactionsViewModel : ViewModel() {
    private val TAG = TransactionsViewModel::class.java.simpleName

    var transactionByMonth: List<Pair<Int, List<Transaction>>> = listOf()
    var transaction: List<Transaction> = listOf()

    val responseStatus = MutableLiveData(Status.IDLE)

    private var mRepository: Repository = Repository()

    init {
        this.mRepository = Repository()
    }

    fun getAccountData(accountNum: Int) {
        responseStatus.value = Status.LOADING

        viewModelScope.launch {
            mRepository.getAccountData(accountNum, object : FetchDataCallback<Transactions> {
                override fun onFailure() {
                    responseStatus.postValue(Status.ERROR)
                }

                override fun onSuccess(data: Transactions) {
                    Log.d(TAG, data.toString())

                    /* save as list */
                    transaction = data.transactions
                    /* sort by name, group by institution name */
                    transactionByMonth =
                        data.transactions.sortedByDescending { transaction ->
                            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())

                            /* sort by date */
                            sdf.parse(transaction.date)
                        }.groupBy { transaction ->
                            val calendar = Calendar.getInstance()
                            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
                            calendar.time = sdf.parse(transaction.date)!!

                            /* group by month */
                            calendar.get(Calendar.MONTH)
                        }.toList()

                    responseStatus.postValue(Status.SUCCESS)
                }
            })
        }
    }
}
