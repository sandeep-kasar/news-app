package com.example.newsapp.ui.main.view

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


class MainActivity : AppCompatActivity(), CountrySelectorDialog.SelectionDialogListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewModel()
        setupUI()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(MainViewModel::class.java)
    }

    private fun setupUI() {
        adapter = MainAdapter(arrayListOf())
        recyclerView.also {
            it.layoutManager = LinearLayoutManager(this)
            it.setHasFixedSize(true)
            it.adapter = adapter
        }

        supportActionBar!!.setDisplayShowCustomEnabled(true);
        supportActionBar!!.setCustomView(R.layout.country_selector)
        val view = supportActionBar!!.customView
        val countryCode = view.findViewById<View>(R.id.tvSelectCountry) as TextView
        view.setOnClickListener {
            CountrySelectorDialog().show(supportFragmentManager,"countrySelector")
        }

        setupObservers(countryCode.text.toString().trim())
    }

    private fun setupObservers(countryCode:String) {
        val apiUrl = RetrofitBuilder.SUB_URL_HEAD + countryCode + RetrofitBuilder.SUB_URL_TAIL
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

    private fun retrieveList(newsArticle: List<NewsArticle>) {
        adapter.apply {
            addNewsArticle(newsArticle)
            notifyDataSetChanged()
        }
    }

    override fun onCountryClick(country: String) {
        val countryCode = supportActionBar!!.customView
            .findViewById<View>(R.id.tvSelectCountry) as TextView
        countryCode.text = country
        setupObservers(country)
    }
}