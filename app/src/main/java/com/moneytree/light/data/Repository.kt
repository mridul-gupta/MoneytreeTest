package com.moneytree.light.data


interface Repository {
    suspend fun getAccounts(force: Boolean = false) : Result<Accounts>
    suspend fun getAccountData(accountNum: Int, force: Boolean = false) : Result<Transactions>
}
