package com.moneytree.light.data


import android.util.Log
import com.moneytree.light.EspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap


class DefaultRepository(
    private val remoteDataSource: DataSource,
    private val localDataSource: DataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : Repository {

    private var cachedAccounts: Accounts? = null
    private var cachedTransactions: ConcurrentMap<Int, Transactions>? = null


    override suspend fun getAccounts(force: Boolean): Result<Accounts> {

        EspressoIdlingResource.setIdleState(false) // Set app as busy.

        return withContext(ioDispatcher) {
            // Respond immediately with cache if available and not dirty
            if (!force) {
                cachedAccounts?.let { cachedAccounts ->
                    EspressoIdlingResource.setIdleState(true) // Set app as idle.
                    return@withContext Result.Success(cachedAccounts)
                }
            }

            val newAccounts = fetchAccountsFromRemoteOrLocal(force)

            //ToDo: Refresh the cache if needed
            (newAccounts as? Result.Success)?.let { refreshCache(it.data) }

            EspressoIdlingResource.setIdleState(true) // Set app as idle.

            //ToDo: return from cache


            (newAccounts as? Result.Success)?.let {
                if (it.data.accounts.isNotEmpty()) {
                    return@withContext Result.Success(it.data)
                }
            }
            return@withContext Result.Error(Exception("Illegal state"))
        }
    }

    private suspend fun fetchAccountsFromRemoteOrLocal(force: Boolean): Result<Accounts> {
        // Remote first
        when (val remoteAccounts = remoteDataSource.getAccounts(force)) {
            is Result.Error -> Log.e(
                "fetchAccountsFromRemoteOrLocal",
                "Remote data source fetch failed"
            )
            is Result.Success -> {
                refreshLocalDataSource(remoteAccounts.data)
                return remoteAccounts
            }
            else -> throw IllegalStateException()
        }

        // Don't read from local if it's forced
        //ToDo: in case of force, don't go to local
        // if (force) {
        //    return Result.Error(Exception("Can't force refresh: remote-data-source didn't work"))
        //}

        // Local if remote fails
        val localAccounts = localDataSource.getAccounts(force)
        if (localAccounts is Result.Success) {
            return localAccounts
        }
        return Result.Error(Exception("Error fetching from remote and local"))
    }


    override suspend fun getAccountData(accountNum: Int, force: Boolean): Result<Transactions> {
        EspressoIdlingResource.setIdleState(false) // Set app as busy.

        return withContext(ioDispatcher) {
            // Respond immediately with cache if available and not dirty
            if (!force) {
                getTransactionsForAccount(accountNum)?.let {
                    EspressoIdlingResource.setIdleState(true) // Set app as idle.
                    return@withContext Result.Success(it)
                }
            }

            val newTransactions = fetchTransactionsFromRemoteOrLocal(accountNum, force)

            //ToDo: Refresh the cache if needed
            (newTransactions as? Result.Success)?.let { cacheTransactions(accountNum, it.data) }

            EspressoIdlingResource.setIdleState(true) // Set app as idle.

            //ToDo: return from cache


            (newTransactions as? Result.Success)?.let {
                if (it.data.transactions.isNotEmpty()) {
                    return@withContext Result.Success(it.data)
                }
            }
            return@withContext Result.Error(Exception("Illegal state"))
        }
    }

    private suspend fun fetchTransactionsFromRemoteOrLocal(
        accountNum: Int,
        force: Boolean
    ): Result<Transactions> {
        // Remote first
        when (val result = remoteDataSource.getAccountData(accountNum, force)) {
            is Result.Error -> Log.e(
                "fetchAccountsFromRemoteOrLocal",
                "Remote data source fetch failed"
            )
            is Result.Success -> {
                refreshLocalDataSource(result.data)
                return result
            }
            else -> throw IllegalStateException()
        }

        // Don't read from local if it's forced
        //ToDo: in case of force, don't go to local
        // if (force) {
        //    return Result.Error(Exception("Can't force refresh: remote-data-source didn't work"))
        //}

        // Local if remote fails
        val localTransactions = localDataSource.getAccountData(accountNum, force)
        if (localTransactions is Result.Success) {
            return localTransactions
        }
        return Result.Error(Exception("Error fetching from remote and local"))
    }

    private fun getTransactionsForAccount(id: Int) = cachedTransactions?.get(id)

    private suspend fun refreshLocalDataSource(transactions: Transactions) {
        //ToDo: refresh local data
    }

    private suspend fun refreshLocalDataSource(accounts: Accounts) {
        //ToDo: refresh local data
    }

    private fun refreshCache(accounts: Accounts) {
        //ToDo: cache management
    }

    private fun refreshCache(transactions: Transactions) {
        //ToDo: cache management
    }

    private fun cacheTransactions(
        accountNum: Int,
        transactions: Transactions
    ) {
        // Create if it doesn't exist.
        if (cachedTransactions == null) {
            cachedTransactions = ConcurrentHashMap()
        }
        cachedTransactions?.put(accountNum, transactions)
    }
}
