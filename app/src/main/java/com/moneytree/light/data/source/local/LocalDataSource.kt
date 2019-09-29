package com.moneytree.light.data.source.local

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.moneytree.light.ACCOUNTS_FILE_NAME
import com.moneytree.light.MoneytreeLight
import com.moneytree.light.R
import com.moneytree.light.data.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream

class LocalDataSource internal constructor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DataSource {


    override suspend fun getAccounts(force: Boolean): Result<Accounts> =
        withContext(ioDispatcher) {
            return@withContext try {
                val jsonString: String?
                val inputStream: InputStream =
                    MoneytreeLight.context!!.assets.open(ACCOUNTS_FILE_NAME)
                jsonString = inputStream.bufferedReader().use { it.readText() }

                val type = object : TypeToken<Accounts>() {}.type
                Result.Success(Gson().fromJson(jsonString, type) as Accounts)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }


    override suspend fun getAccountData(accountNum: Int, force: Boolean): Result<Transactions> =

        withContext(ioDispatcher) {
            try {
                val jsonString: String?
                val filename: String =
                    MoneytreeLight.context!!.resources.getString(
                        R.string.transactions_file_name,
                        accountNum
                    )

                val inputStream: InputStream = MoneytreeLight.context!!.assets.open(filename)

                jsonString = inputStream.bufferedReader().use { it.readText() }

                val type = object : TypeToken<Transactions>() {}.type
                Result.Success(Gson().fromJson(jsonString, type) as Transactions)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
}