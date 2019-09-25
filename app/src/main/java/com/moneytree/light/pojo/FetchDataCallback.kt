package com.moneytree.light.pojo

interface FetchDataCallback<V> {
    fun onFailure()
    fun onSuccess(data: V)
}