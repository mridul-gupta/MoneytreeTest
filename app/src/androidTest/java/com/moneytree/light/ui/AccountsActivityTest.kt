package com.moneytree.light.ui

import androidx.test.filters.LargeTest
import com.moneytree.light.ServiceLocator
import com.moneytree.light.data.Repository
import org.junit.After
import org.junit.Before
import org.junit.Test

@LargeTest
class AccountsActivityTest {
    private lateinit var repository: Repository

    @Before
    fun init() {
        repository =
            ServiceLocator.provideAccountsRepository()
    }

    @After
    fun reset() {
        ServiceLocator.resetRepository()
    }

    @Test
    fun checkAllViewsPresence() {
        //ToDo
    }

    @Test
    fun navigateToTransactions_PressBackToAccountsView() {
        //ToDo
    }

    @Test
    fun addAccountButton_NoAction() {
        //ToDo
    }

    @Test
    fun userProfileButton_NoAction() {
        //ToDo
    }
}