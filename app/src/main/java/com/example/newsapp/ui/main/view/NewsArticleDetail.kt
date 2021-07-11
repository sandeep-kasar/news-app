package com.example.newsapp.ui.main.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapp.R
import com.example.newsapp.data.model.NewsArticle
import com.example.newsapp.databinding.ActivityNewsArticleBinding

/**
 * This activity is showing the details of news article
 *
 * @author SandeepK
 * @version 1.0
 * */
class NewsArticleDetail : AppCompatActivity() {

    private lateinit var binding: ActivityNewsArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
    }

    /**
     * Setup UI
     * */
    private fun setupUI() {

        // set actionbar
        supportActionBar?.title = getString(R.string.title_article)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // bind layout to activity using data binding
        binding = ActivityNewsArticleBinding.inflate(layoutInflater)
        binding.newsArticle = intent.extras?.getSerializable("newsArticle") as NewsArticle
        setContentView(binding.root)
    }
    /**
     * This function is used to navigate back
     * */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}