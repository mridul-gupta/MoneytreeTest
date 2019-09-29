package com.moneytree.light

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.moneytree.light.data.DefaultRepository
import com.moneytree.light.data.Repository
import com.moneytree.light.data.source.local.LocalDataSource
import com.moneytree.light.data.source.remote.RemoteDataSource


object ServiceLocator {
    private val lock = Any()
    @Volatile
    var repository: Repository? = null
        @VisibleForTesting set

    fun provideAccountsRepository(context: Context): Repository {
        synchronized(this) {
            return repository ?: repository ?: createAccountsRepository()
        }
    }

    private fun createAccountsRepository(): Repository {

        return DefaultRepository(
            RemoteDataSource(),
            LocalDataSource() //Database can passed if needed
        )
    }

    @VisibleForTesting
    fun resetRepository() {
        repository = null
    }
}
