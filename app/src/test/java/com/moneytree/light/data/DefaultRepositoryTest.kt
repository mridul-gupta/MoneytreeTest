package com.moneytree.light.data

import com.google.common.truth.Truth.assertThat
import com.moneytree.light.data.Result.Error
import com.moneytree.light.data.Result.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DefaultRepositoryTest {

    /* Subject under test */
    private lateinit var defaultRepository: DefaultRepository

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


    private val remoteAccounts: Accounts = ACCOUNTS_SERVICE_DATA
    private val localAccounts: Accounts = Accounts(listOf())
    private val newAccounts: Accounts = Accounts(listOf())

    private val remoteTransactions: HashMap<Int, Transactions> =
        hashMapOf(1 to transactions_1, 2 to transactions_2, 3 to transactions_3)
    private val localTransactions: HashMap<Int, Transactions> =
        hashMapOf(1 to transactions_1)
    private val newTransactions: HashMap<Int, Transactions> =
        hashMapOf()

    private lateinit var remoteDataSource: FakeDataSource
    private lateinit var localDataSource: FakeDataSource


    @Before
    fun createRepository() {
        remoteDataSource = FakeDataSource(remoteAccounts, remoteTransactions)
        localDataSource = FakeDataSource(localAccounts, localTransactions)

        // Get a reference to the class under test
        defaultRepository = DefaultRepository(
            remoteDataSource, localDataSource, Dispatchers.Unconfined
        )
    }

    @Test
    fun getAccounts_emptyRepositoryAndUninitializedCache() = runBlockingTest {
        val emptySource = FakeDataSource()
        val tasksRepository = DefaultRepository(
            emptySource, emptySource, Dispatchers.Unconfined
        )
        assertThat(tasksRepository.getAccounts() is Success).isTrue()
    }

    @Test
    fun getAccounts_repositoryCachesAfterFirstApiCall() = runBlockingTest {
        // Trigger the repository to load data, which loads from remote and caches
        val initial = defaultRepository.getAccounts()

        remoteDataSource.accounts = newAccounts

        val second = defaultRepository.getAccounts()

        // Initial and second should match because we didn't force a refresh
        assertThat(second).isEqualTo(initial)
    }

    @Test
    fun getAccounts_requestsAllAccountsFromRemoteDataSource() = runBlockingTest {
        // When accounts are requested from the tasks repository
        val result = defaultRepository.getAccounts() as Success

        // Then accounts are loaded from the remote data source
        assertThat(result.data).isEqualTo(remoteAccounts)
    }

    @Test
    fun getAccounts_WithDirtyCache_remoteUnavailable_error() = runBlockingTest {
        // Make remote data source unavailable
        remoteDataSource.accounts = null

        // Load accounts forcing remote load
        val refreshedAccounts = defaultRepository.getAccounts(true)

        // Result should be an error
        assertThat(refreshedAccounts).isInstanceOf(Error::class.java)
    }

    @Test
    fun getAccounts_WithRemoteDataSourceUnavailable_dataRetrievedFromLocal() = runBlockingTest {
        // When the remote data source is unavailable
        remoteDataSource.accounts = null

        // The repository fetches from the local source
        assertThat((defaultRepository.getAccounts() as Success).data).isEqualTo(localAccounts)
    }

    @Test
    fun getAccounts_WithBothDataSourcesUnavailable_returnsError() = runBlockingTest {
        // When both sources are unavailable
        remoteDataSource.accounts = null
        localDataSource.accounts = null

        // The repository returns an error
        assertThat(defaultRepository.getAccounts()).isInstanceOf(Error::class.java)
    }

    @Test
    fun getAccounts_refreshesLocalDataSource() = runBlockingTest {
        //ToDo: localdatasource cant be saved
    }


    /**
     * Transactions
     */

    @Test
    fun getAccountData_emptyRepositoryAndUninitializedCache() = runBlockingTest {
        val emptySource = FakeDataSource()
        val repository = DefaultRepository(
            emptySource, emptySource, Dispatchers.Unconfined
        )
        assertThat(repository.getAccountData(1) is Error).isTrue()
    }

    @Test
    fun getAccountData_repositoryCachesAfterFirstApiCall() = runBlockingTest {
        // Trigger the repository to load data, which loads from remote and caches
        val initial = defaultRepository.getAccountData(2)

        remoteDataSource.accounts = newAccounts

        val second = defaultRepository.getAccountData(2)

        // Initial and second should match because we didn't force a refresh
        assertThat(second).isEqualTo(initial)
    }


    @Test
    fun getAccountData_WithDirtyCache_remoteUnavailable_error() = runBlockingTest {
        // Make remote data source unavailable
        remoteDataSource.transactions = null

        // Load accounts forcing remote load
        val refreshedAccounts = defaultRepository.getAccountData(2, true)

        // Result should be an error
        assertThat(refreshedAccounts).isInstanceOf(Error::class.java)
    }

    @Test
    fun getAccountData_WithRemoteDataSourceUnavailable_dataRetrievedFromLocal() = runBlockingTest {
        // When the remote data source is unavailable
        remoteDataSource.transactions = null

        // The repository fetches from the local source
        assertThat(defaultRepository.getAccountData(1) is Success).isTrue()
    }

    @Test
    fun getAccountData_WithBothDataSourcesUnavailable_returnsError() = runBlockingTest {
        // When both sources are unavailable
        remoteDataSource.transactions = null
        localDataSource.transactions = null

        // The repository returns an error
        assertThat(defaultRepository.getAccountData(1)).isInstanceOf(Error::class.java)
    }

    @Test
    fun getAccountData_refreshesLocalDataSource() = runBlockingTest {
        //ToDo: localdatasource cant be saved
    }
}