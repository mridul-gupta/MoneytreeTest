package com.moneytree.light

import android.app.Application


class MoneytreeLight : Application() {

    private lateinit var mContext: MoneytreeLight

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        var context: MoneytreeLight? = null
            private set
    }
}