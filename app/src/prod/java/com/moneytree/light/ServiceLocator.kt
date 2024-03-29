package com.moneytree.light

import androidx.annotation.VisibleForTesting
import com.moneytree.light.data.DefaultRepository
import com.moneytree.light.data.Repository
import com.moneytree.light.data.source.local.LocalDataSource
import com.moneytree.light.data.source.remote.RemoteDataSource


object ServiceLocator {
    @Volatile
    var repository: Repository? = null
        @VisibleForTesting set

    fun provideAccountsRepository(): Repository {
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
