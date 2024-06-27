package com.test.test.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoaderViewModel : ViewModel()
{
    private val _loader = MutableLiveData<Boolean>()
    val loader : LiveData<Boolean>
        get() = _loader

    fun showLoader()
    {
        _loader.value = true
    }

    fun hideLoader() = _loader.let {
        it.value = false
    }
}
