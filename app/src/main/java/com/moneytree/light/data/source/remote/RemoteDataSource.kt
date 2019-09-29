package com.moneytree.light.data.source.remote

import com.moneytree.light.data.*
import java.lang.Exception

class RemoteDataSource : DataSource {
    override suspend fun getAccounts(force: Boolean): Result<Accounts> = Result.Error(Exception())
    //TODO("not implemented. network call to get accounts")

    override suspend fun getAccountData(accountNum: Int, force: Boolean): Result<Transactions> =
        Result.Error(Exception())
    //TODO("not implemented. network call to get account transactions.")
}