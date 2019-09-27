package com.moneytree.light


const val ACCOUNTS_FILE_NAME: String = "accounts.json"

const val ACCOUNT_DASHBOARD_SCREEN = 10001
const val ACCOUNT_TRANSACTIONS_SCREEN = 10002



enum class Status {
    IDLE,
    LOADING,
    SUCCESS,
    ERROR
}
