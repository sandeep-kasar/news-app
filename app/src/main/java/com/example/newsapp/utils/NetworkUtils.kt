package com.example.newsapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


/**
 * Network Utility to detect availability or unavailability of Internet connection
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
object NetworkUtils : ConnectivityManager.NetworkCallback()
{
    private val networkLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var isConnected = false

    /**
     * Returns instance of [LiveData] which can be observed for network changes.
     */
    fun getNetworkLiveData(context: Context): LiveData<Boolean> {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(this)
        }else {
            connectivityManager.registerNetworkCallback(
                NetworkRequest.Builder().build(), this)
        }

        // Retrieve current status of connectivity
        connectivityManager.allNetworks.forEach { network ->
            val networkCapability = connectivityManager.getNetworkCapabilities(network)
            networkCapability?.let {
                if (it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                    isConnected = true
                    return@forEach
                }
            }
        }

        networkLiveData.postValue(isConnected)

        return networkLiveData
    }

    override fun onAvailable(network: Network) {
        networkLiveData.postValue(true)
    }

    override fun onLost(network: Network) {
        networkLiveData.postValue(false)
    }
}