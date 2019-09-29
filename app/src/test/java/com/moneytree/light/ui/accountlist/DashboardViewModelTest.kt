package com.moneytree.light.ui.accountlist

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
class DashboardViewModelTest {

    /* Subject under test */
    private lateinit var dashboardViewModel: DashboardViewModel

    /* use a fake repository to inject into viewmodel */
    private lateinit var repository: FakeRepository


    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule var instantExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setup() {
        repository = FakeRepository()
        dashboardViewModel = DashboardViewModel(repository)
    }

    @Test
    fun loadAllAccounts_progressIndicatorAndDataLoaded() {
        // Pause dispatcher so we can verify initial values
        mainCoroutineRule.pauseDispatcher()

        // Trigger loading of accounts
        dashboardViewModel.getAccounts(true)

        // Then progress indicator is shown
        assert(LiveDataTestUtil.getValue(dashboardViewModel.responseStatus) == Status.LOADING)

        // Execute pending coroutines actions
        mainCoroutineRule.resumeDispatcher()

        // Then progress indicator is hidden
        assert(LiveDataTestUtil.getValue(dashboardViewModel.responseStatus) != Status.LOADING)

        // And data correctly loaded
        assert(dashboardViewModel.accounts.size == 3)
    }

    @Test
    fun loadAllAccounts_Error() {
        // Make the repository return errors
        repository.setReturnError(true)

        // Trigger loading of accounts
        dashboardViewModel.getAccounts(true)

        // Then progress indicator is hidden
        assert(LiveDataTestUtil.getValue(dashboardViewModel.responseStatus) != Status.LOADING)

        // And data size is zero
        assert(dashboardViewModel.accounts.isEmpty())
    }
}