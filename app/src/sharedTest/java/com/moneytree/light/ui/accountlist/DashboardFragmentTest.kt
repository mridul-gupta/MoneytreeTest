package com.moneytree.light.ui.accountlist

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.MediumTest
import com.moneytree.light.ServiceLocator
import com.moneytree.light.data.Repository
import com.moneytree.light.data.source.FakeRepository
import com.moneytree.light.ui.AccountsActivity
import org.junit.After
import org.junit.Before
import org.junit.Test


@MediumTest
class DashboardFragmentTest {
    private lateinit var repository: Repository

    @Before
    fun initRepository() {
        repository = FakeRepository()
        ServiceLocator.repository = repository
    }


    @After
    fun cleanup() {
        ServiceLocator.resetRepository()
    }

    @Test
    fun displayAccount_whenRepositoryHasData() {
        // GIVEN - One account already in the repository

        // WHEN - On startup
        ActivityScenario.launch(AccountsActivity::class.java)

        // THEN - Verify task is displayed on screen
        Espresso.onView(withText("Starbucks Card"))
            .check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun showAllAccounts() {
        // Add one active task and one completed task
        ActivityScenario.launch(AccountsActivity::class.java)

        // Verify that all accounts are shown
        Espresso.onView(withText("マイカード"))
            .check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(withText("マークからカード"))
            .check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(withText("外貨普通(USD)"))
            .check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun clickAccount_navigateToTransactionsFragment() {
        //ToDo: unable to run instrumentation
    }
}
