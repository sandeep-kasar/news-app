package com.example.newsapp.ui.main.view

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.data.api.ApiHelper
import com.example.newsapp.data.api.RetrofitBuilder
import com.example.newsapp.data.model.NewsArticle
import com.example.newsapp.data.room.NewsDatabase
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.ui.base.ViewModelFactory
import com.example.newsapp.ui.main.adapter.MainAdapter
import com.example.newsapp.ui.main.viewmodel.MainViewModel
import com.example.newsapp.utils.dialog.CountrySelectorDialog
import com.example.newsapp.data.room.NewsMapper
import com.example.newsapp.utils.NetworkUtils
import com.example.newsapp.utils.response.Status.*
import com.example.newsapp.utils.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * This activity is the starting point of the application
 *
 * @author SandeepK
 * @version 1.0
 * */

class MainActivity : AppCompatActivity(), CountrySelectorDialog.SelectionDialogListener, NewsMapper {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainAdapter
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var newsDatabase: NewsDatabase

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
        setupUI()
    }

    /**
     * Init viewModel
     *
     * */
    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(MainViewModel::class.java)
    }

    /**
     * setup UI,
     * Init adapter, recyclerView
     * Set actionbar
     * and handle click events
     *
     * */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setupUI() {

        // init
        newsDatabase = NewsDatabase.buildDefault(applicationContext)

        //binding layout
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        // adapter setting
        adapter = MainAdapter(arrayListOf())
        mainBinding.recyclerView.also {
            it.layoutManager = LinearLayoutManager(this)
            it.setHasFixedSize(true)
            it.adapter = adapter
        }

        // create custom actionbar
        supportActionBar!!.setDisplayShowCustomEnabled(true);
        supportActionBar!!.setCustomView(R.layout.country_selector)
        val view = supportActionBar!!.customView
        val countryCode = view.findViewById<View>(R.id.tvSelectCountry) as TextView

        // save users selected country for later use
        saveUsersChoice(countryCode.text.toString().trim())

        // create country selection dialog
        view.setOnClickListener {
            CountrySelectorDialog().show(supportFragmentManager,"countrySelector")
        }

        // refresh layout setting
        mainBinding.swipeRefreshLayout.setOnRefreshListener {
             setupObservers(countryCode.text.toString().trim())
        }

    }

    /**
     * Define observer to observe API call data from viewModel/Livedata
     *
     * Check internet connection, If network is available then call API
     * else show data from local database/room
     *
     * */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setupObservers(country:String) {

        // set users country
        val tvCountryCode = supportActionBar!!.customView
            .findViewById<View>(R.id.tvSelectCountry) as TextView
        tvCountryCode.text = country

        // disable swipeRefreshLayout
        if (mainBinding.swipeRefreshLayout.isRefreshing) {
            mainBinding.swipeRefreshLayout.isRefreshing = false;
        }

        // generate API call url
        val apiUrl = RetrofitBuilder.SUB_URL_HEAD + country + RetrofitBuilder.SUB_URL_TAIL

        // check network connection and then call API
        NetworkUtils.getNetworkLiveData(applicationContext).observe(this, Observer { isConncted ->
            if (isConncted) {
                // Call to news API
                viewModel.getTopHeadlines(apiUrl).observe(this, Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            SUCCESS -> {
                                mainBinding.recyclerView.visibility = View.VISIBLE
                                mainBinding.progressBar.visibility = View.GONE
                                resource.data?.let { newsResponse -> retrieveList(newsResponse.articles) }
                            }
                            ERROR -> {
                                mainBinding.recyclerView.visibility = View.VISIBLE
                                mainBinding.progressBar.visibility = View.GONE
                                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                            }
                            LOADING -> {
                                mainBinding.progressBar.visibility = View.VISIBLE
                                mainBinding.recyclerView.visibility = View.GONE
                            }
                        }
                    }
                })
            }else {

                // show local data if there is no internet connection
                // do task on IO thread
                lifecycleScope.launch(Dispatchers.IO) {
                    if (newsDatabase.newsArticlesDao().getNewsArticles().isNotEmpty()) {
                        val newsArticleDatabase = newsDatabase.newsArticlesDao().getNewsArticles()
                        val newsArticles = newsArticleDatabase.toRemote()
                        runOnUiThread {
                            mainBinding.recyclerView.visibility = View.VISIBLE
                            mainBinding.progressBar.visibility = View.GONE
                            retrieveList(newsArticles)
                        }
                    } else {
                        runOnUiThread {
                            mainBinding.recyclerView.visibility = View.VISIBLE
                            mainBinding.progressBar.visibility = View.GONE
                            toast("Empty data",Toast.LENGTH_LONG)
                        }
                    }
                }
            }
        })
    }

    /**
     * Display the news response data
     *
     * @param newsArticle - list of news articles
     *
     * */
    private fun retrieveList(newsArticle: List<NewsArticle>) {
        adapter.apply {
            addNewsArticle(newsArticle)
            notifyDataSetChanged()
        }

        // save data in local db
        lifecycleScope.launch(Dispatchers.IO) {
            newsArticle?.toStorage()?.let { newsDatabase.newsArticlesDao().clearAndCacheArticles(it) }
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCountryClick(country: String) {
        saveUsersChoice(country)
    }

    /**
     * Save users country in shared pref
     * Also make API call with same country
     *
     * @param countryCode - users selected country code
     *
     * */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun saveUsersChoice(countryCode: String){

        // check users country is available or not in shared pref
        val sharedPref = this?.getPreferences(Context.MODE_PRIVATE) ?: return
        var country = sharedPref.getString("countryCode", "-1")

        // if country code is not present
        if (country == "-1"){
            with (sharedPref.edit()) {
                putString("countryCode", countryCode)
                apply()
            }
            country = countryCode
        }

        // when country code is present
        if (country != "-1"){
            with (sharedPref.edit()) {
                putString("countryCode", countryCode)
                apply()
            }
        }

        // make network call with selected country
        country?.let { setupObservers(it) }

    }

}