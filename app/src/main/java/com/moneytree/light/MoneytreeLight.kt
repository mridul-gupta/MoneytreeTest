package com.moneytree.light

import android.app.Application
import com.moneytree.light.data.Repository


class MoneytreeLight : Application() {

    private lateinit var mContext: MoneytreeLight
    val repository: Repository = ServiceLocator.provideAccountsRepository(this)

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        var context: MoneytreeLight? = null
            private set
    }
}