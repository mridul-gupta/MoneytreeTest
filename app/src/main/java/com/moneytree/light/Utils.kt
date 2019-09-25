package com.moneytree.light


val ACCOUNTS_FILE_NAME: String = "accounts.json"
val TRANSACTIONS_BASE_FILE_NAME: String = "transactions_"

enum class Status {
    IDLE,
    LOADING,
    SUCCESS,
    ERROR
}
