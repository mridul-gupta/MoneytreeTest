package com.moneytree.light.ui.accountlist

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import androidx.test.rule.ActivityTestRule
import com.moneytree.light.ServiceLocator
import com.moneytree.light.data.Repository
import com.moneytree.light.data.source.FakeRepository
import com.moneytree.light.ui.AccountsActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@MediumTest
class DashboardFragmentTest {
    private lateinit var repository: Repository
    @Rule
    @JvmField
    var rule: ActivityTestRule<AccountsActivity> = ActivityTestRule(AccountsActivity::class.java)
    private lateinit var accountsActivity: AccountsActivity

    @Before
    fun initRepository() {
        repository = FakeRepository()
        ServiceLocator.repository = repository

        accountsActivity= rule.activity

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
        Espresso.onView(ViewMatchers.withText("Starbucks Card"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}
