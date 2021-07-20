package com.example.newsapp.utils.response

/**
 * This is the data class to return response
 *
 * @param status - indicate the api response status as  SUCCESS,ERROR,LOADING
 *
 * @param data - this is the response data which we get from API call
 *
 * @param message - response message
 *
 * @author SandeepK
 * @version 1.0
 * */
data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): Resource<T> = Resource(status = Status.SUCCESS, data = data, message = null)

        fun <T> error(data: T?, message: String): Resource<T> =
            Resource(status = Status.ERROR, data = data, message = message)

        fun <T> loading(data: T?): Resource<T> = Resource(status = Status.LOADING, data = data, message = null)
    }
}