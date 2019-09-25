package com.moneytree.light.pojo

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.moneytree.light.ACCOUNTS_FILE_NAME
import com.moneytree.light.MoneytreeLight
import com.moneytree.light.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream


class Repository {

    suspend fun getAccounts(fetchDataCallback: FetchDataCallback<Accounts>) {
        /**
         * Decide where data is to be fetched from.
         * Remote Data Source
         * Local DB Data Source
         * Local Asset Data Source
         **/
        withContext(Dispatchers.IO) {
            try {

                //ToDO Do all this in background
                val jsonString: String?
                val inputStream: InputStream =
                    MoneytreeLight.context!!.assets.open(ACCOUNTS_FILE_NAME)
                jsonString = inputStream.bufferedReader().use { it.readText() }

                val type = object : TypeToken<Accounts>() {}.type
                fetchDataCallback.onSuccess(Gson().fromJson(jsonString, type))
            } catch (e: Exception) {
                fetchDataCallback.onFailure()
            }
        }
    }


    suspend fun getAccountData(
        accountNum: Int,
        fetchDataCallback: FetchDataCallback<Transactions>
    ) {
        /**
         * Decide where data is to be fetched from.
         * Remote Data Source
         * Local DB Data Source
         * Local Asset Data Source
         **/
        withContext(Dispatchers.IO) {
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
                fetchDataCallback.onSuccess(Gson().fromJson(jsonString, type))
            } catch (e: Exception) {
                fetchDataCallback.onFailure()
            }
        }
    }
}
