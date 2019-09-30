package com.moneytree.light.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.moneytree.light.ACCOUNT_TRANSACTIONS_SCREEN
import com.moneytree.light.ACCOUNT_DASHBOARD_SCREEN
import com.moneytree.light.R
import com.moneytree.light.ui.accountlist.DashboardFragment
import com.moneytree.light.ui.transactions.TransactionsFragment

class AccountsActivity : AppCompatActivity() {
    private val TAG = AccountsActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.accounts_activity)

        replaceFragment(DashboardFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(android.R.id.content, fragment)
        transaction.commitAllowingStateLoss()
    }

    fun loadFragment(fragmentId: Int, bundle: Bundle?) {
        when (fragmentId) {
            ACCOUNT_DASHBOARD_SCREEN -> {
                replaceFragment(DashboardFragment())
            }

            ACCOUNT_TRANSACTIONS_SCREEN -> {
                val transactionsFragment = TransactionsFragment()
                transactionsFragment.arguments = bundle
                replaceFragment(transactionsFragment)
            }
            else -> Log.e(TAG, "Non existent fragment")
        }
    }
}
