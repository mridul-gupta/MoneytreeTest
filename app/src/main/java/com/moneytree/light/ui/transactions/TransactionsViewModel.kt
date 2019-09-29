package com.moneytree.light.ui.transactions

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneytree.light.Status
import com.moneytree.light.data.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class TransactionsViewModel(
    private val repository: Repository
) : ViewModel() {
    private val TAG = TransactionsViewModel::class.java.simpleName

    var transactionByMonth: Map<String, List<Transaction>> = mapOf()
    var transaction: List<Transaction> = listOf()
    var selectedAccount: Account = Account(0, "", "", "", 0f, 0f)

    val responseStatus = MutableLiveData(Status.IDLE)

    fun getCombinedDataForAdaptor(): MutableList<Comparable<*>> {
        val combineList: MutableList<Comparable<*>> = ArrayList()

        for ((title, tList) in transactionByMonth) {
            println("$title = $tList")

            combineList.add(title)
            tList.forEach {
                combineList.add(it)
            }
        }
        return combineList
    }

    fun getAccountData(accountNum: Int, force: Boolean) {
        responseStatus.value = Status.LOADING

        viewModelScope.launch {
            val result = repository.getAccountData(accountNum, force)
            if (result is Result.Success) {
                Log.d(TAG, result.data.toString())

                /* save as list */
                transaction = result.data.transactions
                /* sort by name, group by institution name */
                transactionByMonth =
                    result.data.transactions.sortedByDescending { transaction ->
                        val sdf =
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())

                        /* sort by date */
                        sdf.parse(transaction.date)
                    }.groupBy { transaction ->
                        val calendar = Calendar.getInstance()
                        val sdf =
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
                        calendar.time = sdf.parse(transaction.date)!!

                        val monthDate = SimpleDateFormat("MMMM", Locale.getDefault())
                        val monthName = monthDate.format(calendar.time)
                        val year = calendar.get(Calendar.YEAR)

                        /* group by month & year*/
                        "$monthName $year"
                    }

                responseStatus.postValue(Status.SUCCESS)
            } else {
                responseStatus.postValue(Status.ERROR)
                transaction = emptyList()
                transactionByMonth = emptyMap()
            }
        }
    }
}
