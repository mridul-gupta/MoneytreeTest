package com.moneytree.light.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class ApiResponse<T>(
    var statusCode: Int,
    var message: String,
    var result: T? = null
)

data class Transaction(
    var account_id: Int,
    var amount: Float,
    var category_id: Int,
    var date: String,
    var description: String,
    var id: Int
) : Comparable<Transaction> {
    override fun compareTo(other: Transaction): Int {
        return 0
    }
}

data class Transactions(
    var transactions: List<Transaction>
)

@Parcelize
data class Account(
    var id: Int,
    var name: String,
    var institution: String,
    var currency: String,
    var current_balance: Float,
    var current_balance_in_base: Float
) : Parcelable

data class Accounts(
    var accounts: List<Account>
)
