package com.moneytree.light


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.moneytree.light.ui.accountlist.DashboardViewModel
import com.moneytree.light.ui.transactions.TransactionsViewModel

class ViewModelFactory private constructor() : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel() as T
        } else if (modelClass.isAssignableFrom(TransactionsViewModel::class.java)) {
            return TransactionsViewModel() as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(): ViewModelFactory? {

            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE =
                            ViewModelFactory()
                    }
                }
            }
            return INSTANCE
        }
    }
}