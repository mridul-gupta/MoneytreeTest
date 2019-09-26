package com.moneytree.light.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.moneytree.light.R
import com.moneytree.light.ui.accountlist.DashboardFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.accounts_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, DashboardFragment.newInstance())
                .commitNow()
        }
    }

}
