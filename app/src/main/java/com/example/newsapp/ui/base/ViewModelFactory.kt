package com.example.newsapp.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.data.api.ApiHelper
import com.example.newsapp.data.repository.MainRepository
import com.example.newsapp.ui.main.viewmodel.MainViewModel

/**
 * This is the factory class which helps in API call
 *
 * @param apiHelper - helper class to call API
 *
 * @author SandeepK
 * @version 1.0
 *
 * */
class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(MainRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}

