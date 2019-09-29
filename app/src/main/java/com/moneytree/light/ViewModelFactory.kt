package com.moneytree.light


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.moneytree.light.data.Repository
import com.moneytree.light.ui.accountlist.DashboardViewModel
import com.moneytree.light.ui.transactions.TransactionsViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val repository: Repository
) : ViewModelProvider.NewInstanceFactory() {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return with(modelClass) {
            when {
                isAssignableFrom(DashboardViewModel::class.java) ->
                    DashboardViewModel(repository)
                isAssignableFrom(TransactionsViewModel::class.java) ->
                    TransactionsViewModel(repository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
    }
}