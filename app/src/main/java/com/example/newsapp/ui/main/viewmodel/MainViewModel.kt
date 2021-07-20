package com.example.newsapp.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.newsapp.data.repository.MainRepository
import com.example.newsapp.utils.response.Resource
import kotlinx.coroutines.Dispatchers

/**
 * This is the viewModel class which hold he MainActivity data
 * Coroutines is used to handle network call
 *
 * @param mainRepository - repository which handle API call
 *
 * @author SandeepK
 * @version 1.0
 * */
class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    fun getTopHeadlines(url:String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getTopHeadlines(url)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}