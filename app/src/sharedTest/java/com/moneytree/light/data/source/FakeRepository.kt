package com.moneytree.light.data.source

import com.moneytree.light.data.*

class FakeRepository : Repository {
    private var shouldReturnError = false

    private var ACCOUNTS_SERVICE_DATA: Accounts =
        Accounts(
            listOf(
                Account(
                    1,
                    "外貨普通(USD)",
                    "Test Bank",
                    "USD",
                    22.5f,
                    2306.0f
                ),
                Account(
                    2,
                    "tマークからカード",
                    "Starbucks Card",
                    "JPY",
                    3035.0f,
                    3035.0f
                ),
                Account(
                    3,
                    "sマイカード",
                    "Starbucks Card",
                    "JPY",
                    0.0f,
                    0.0f
                )
            )
        )
    private var transactions_1: Transactions =
        Transactions(
            listOf(
                Transaction(
                    1,
                    -6850.0f,
                    192,
                    "2017-08-22T00:00:00+09:00",
                    "\u53d6\u5f15\u660e\u7d30\u540d 2017-08-22T17:19:31+09:00",
                    11
                )
            )
        )

    private var transactions_2: Transactions =
        Transactions(
            listOf(
                Transaction(
                    1,
                    -6850.0f,
                    192,
                    "2017-08-22T00:00:00+09:00",
                    "\u53d6\u5f15\u660e\u7d30\u540d 2017-08-22T17:19:31+09:00",
                    11
                ),
                Transaction(
                    2,
                    -442.0f,
                    112,
                    "2017-05-26T00:00:00+09:00",
                    "\u30b9\u30bf\u30fc\u30d0\u30c3\u30af\u30b9 \u539f\u5bbf\u5e97",
                    21
                ),
                Transaction(
                    2,
                    -442.0f,
                    112,
                    "2017-05-24T00:00:00+09:00",
                    "\u30b9\u30bf\u30fc\u30d0\u30c3\u30af\u30b9 \u539f\u5bbf\u5e97",
                    22
                ),
                Transaction(
                    2,
                    -421.0f,
                    112,
                    "2017-05-23T00:00:00+09:00",
                    "\u30b9\u30bf\u30fc\u30d0\u30c3\u30af\u30b9 \u539f\u5bbf\u5e97",
                    23
                ),
                Transaction(
                    2,
                    5000.0f,
                    112,
                    "2017-04-19T00:00:00+09:00",
                    "\u30b9\u30bf\u30fc\u30d0\u30c3\u30af\u30b9 \u539f\u5bbf\u5e97",
                    24
                ),
                Transaction(
                    2,
                    -1047.0f,
                    112,
                    "2017-04-19T00:00:00+09:00",
                    "\u30b9\u30bf\u30fc\u30d0\u30c3\u30af\u30b9 \u539f\u5bbf\u5e97",
                    25
                )
            )
        )

    private var transactions_3: Transactions =
        Transactions(
            listOf()
        )

    private var TRANSACTIONS_SERVICE_DATA: HashMap<Int, Transactions> = HashMap()

    init {
        TRANSACTIONS_SERVICE_DATA[1] = transactions_1
        TRANSACTIONS_SERVICE_DATA[2] = transactions_2
        TRANSACTIONS_SERVICE_DATA[3] = transactions_3
    }

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun getAccounts(force: Boolean): Result<Accounts> {

        if (shouldReturnError) {
            return Result.Error(Exception("Test exception"))
        }
        return Result.Success(ACCOUNTS_SERVICE_DATA)
    }

    override suspend fun getAccountData(accountNum: Int, force: Boolean): Result<Transactions> {
        if (shouldReturnError) {
            return Result.Error(Exception("Test exception"))
        }

        TRANSACTIONS_SERVICE_DATA[accountNum]?.let {
            return Result.Success(it)
        }
        return Result.Error(Exception("Could not find account"))
    }
}