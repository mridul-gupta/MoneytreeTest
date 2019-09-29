package com.moneytree.light.data


interface DataSource {

    suspend fun getAccounts(force: Boolean): Result<Accounts>

    suspend fun getAccountData(accountNum: Int, force: Boolean): Result<Transactions>
}
