package com.moneytree.light.ui.transactions

import androidx.test.filters.MediumTest
import com.moneytree.light.ServiceLocator
import com.moneytree.light.data.Repository
import com.moneytree.light.data.source.FakeRepository
import org.junit.After
import org.junit.Before
import org.junit.Test


@MediumTest
class TransactionsFragmentTest {
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
    fun displayAccountTransactions_whenRepositoryHasData() {
        // GIVEN - One account already in the repository

        /**
         * ToDo: Instrumentaion not working
         * Click on the account to navigate to Transactions fragment
         * verify transactions are displayed on screen
         * verify adaptor has matching number of items
         * verify text on the first and last item of recyclerview
         */
    }

    @Test
    fun displayAccountTransactions_whenNoTransactions() {
        // GIVEN - One account already in the repository

        /**
         * ToDo: Instrumentaion not working
         * Click on the account to navigate to Transactions fragment
         * verify adaptor has zero items
         * verify "No Transactions" message is displayed
         */
    }
}
