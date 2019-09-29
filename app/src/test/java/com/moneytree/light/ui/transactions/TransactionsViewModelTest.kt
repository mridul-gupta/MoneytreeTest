package com.moneytree.light.ui.transactions

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.moneytree.light.LiveDataTestUtil
import com.moneytree.light.MainCoroutineRule
import com.moneytree.light.Status
import com.moneytree.light.data.source.FakeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TransactionsViewModelTest {

    /* Subject under test */
    private lateinit var transactionsViewModel: TransactionsViewModel

    /* use a fake repository to inject into viewmodel */
    private lateinit var repository: FakeRepository


    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setup() {
        repository = FakeRepository()
        transactionsViewModel = TransactionsViewModel(repository)
    }

    @Test
    fun loadTransactions_ID2_LoadData() {
        // Pause dispatcher so we can verify initial values
        mainCoroutineRule.pauseDispatcher()

        // Trigger loading of accounts
        transactionsViewModel.getAccountData(2, true)

        // Then progress indicator is shown
        assert(LiveDataTestUtil.getValue(transactionsViewModel.responseStatus) == Status.LOADING)

        // Execute pending coroutines actions
        mainCoroutineRule.resumeDispatcher()

        // Then progress indicator is hidden
        assert(LiveDataTestUtil.getValue(transactionsViewModel.responseStatus) != Status.LOADING)

        // And data correctly loaded
        assert(transactionsViewModel.transactionList.size == 6)
        assert(transactionsViewModel.transactionListByMonth.size == 3)
    }

    @Test
    fun loadTransactions_ID3_EmptyData() {
        // Pause dispatcher so we can verify initial values
        mainCoroutineRule.pauseDispatcher()

        // Trigger loading of accounts
        transactionsViewModel.getAccountData(3, true)

        // Then progress indicator is shown
        assert(LiveDataTestUtil.getValue(transactionsViewModel.responseStatus) == Status.LOADING)

        // Execute pending coroutines actions
        mainCoroutineRule.resumeDispatcher()

        // Then progress indicator is hidden
        assert(LiveDataTestUtil.getValue(transactionsViewModel.responseStatus) != Status.LOADING)

        // And data correctly loaded
        assert(transactionsViewModel.transactionList.isEmpty())
        assert(transactionsViewModel.transactionListByMonth.isEmpty())
    }

    @Test
    fun loadTransactions_Error() {
        // Make the repository return errors
        repository.setReturnError(true)

        // Trigger loading of accounts
        transactionsViewModel.getAccountData(1, false)

        // Then progress indicator is hidden
        assert(LiveDataTestUtil.getValue(transactionsViewModel.responseStatus) != Status.LOADING)

        // And data size is zero
        assert(transactionsViewModel.transactionList.isEmpty())
        assert(transactionsViewModel.transactionListByMonth.isEmpty())
    }
}

