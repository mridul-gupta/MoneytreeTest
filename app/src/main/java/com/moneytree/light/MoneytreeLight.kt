package com.moneytree.light

import android.app.Application
import com.moneytree.light.data.Repository


class MoneytreeLight : Application() {

    val repository: Repository = ServiceLocator.provideAccountsRepository()

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        var context: MoneytreeLight? = null
            private set
    }
}