package com.moneytree.light.data

interface FetchDataCallback<V> {
    fun onFailure()
    fun onSuccess(data: V)
}