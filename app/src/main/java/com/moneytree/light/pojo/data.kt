package com.moneytree.light.pojo

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
)

data class Transactions(
    var transactions: List<Transaction>
)

data class Account(
    var id: Int,
    var name: String,
    var institution: String,
    var currency: String,
    var current_balance: Float,
    var current_balance_in_base: Float
)

data class Accounts(
    var accounts: List<Account>
)
