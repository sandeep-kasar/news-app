package com.example.newsapp.ui.main.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.data.api.ApiHelper
import com.example.newsapp.data.api.RetrofitBuilder
import com.example.newsapp.data.model.NewsArticle
import com.example.newsapp.ui.base.ViewModelFactory
import com.example.newsapp.ui.main.adapter.MainAdapter
import com.example.newsapp.ui.main.viewmodel.MainViewModel
import com.example.newsapp.utils.CountrySelectorDialog
import com.example.newsapp.utils.Status.*
import kotlinx.android.synthetic.main.activity_main.*

/**
 * This activity is the starting point of the application
 *
 * @author SandeepK
 * @version 1.0
 * */

class MainActivity : AppCompatActivity(), CountrySelectorDialog.SelectionDialogListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
    private fun setupUI() {

        // adapter setting
        adapter = MainAdapter(arrayListOf())
        recyclerView.also {
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
        swipeRefreshLayout.setOnRefreshListener {
             setupObservers(countryCode.text.toString().trim())
        }

    }

    /**
     * Define observer to observe API call data from viewModel/Livedata
     *
     * Call to news API
     * */
    private fun setupObservers(country:String) {

        // set users country
        val tvCountryCode = supportActionBar!!.customView
            .findViewById<View>(R.id.tvSelectCountry) as TextView
        tvCountryCode.text = country

        // disable swipeRefreshLayout
        if (swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false;
        }

        // generate API call url
        val apiUrl = RetrofitBuilder.SUB_URL_HEAD + country + RetrofitBuilder.SUB_URL_TAIL

        // Call to news API
        viewModel.getTopHeadlines(apiUrl).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    SUCCESS -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        resource.data?.let { newsResponse -> retrieveList(newsResponse.articles) }
                    }
                    ERROR -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
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
    }

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