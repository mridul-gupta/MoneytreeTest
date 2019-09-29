package com.moneytree.light

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>): T {
    val repository = (requireContext().applicationContext as MoneytreeLight).repository
    return ViewModelProviders.of(this, ViewModelFactory(repository)).get(viewModelClass)
}
