package com.example.newsapp.ui.main.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapp.R
import com.example.newsapp.data.model.NewsArticle
import com.example.newsapp.databinding.ActivityNewsArticleBinding

class NewsArticleDetail : AppCompatActivity() {

    private lateinit var binding: ActivityNewsArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
    }
    private fun setupUI() {

        supportActionBar?.title = getString(R.string.title_article)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = ActivityNewsArticleBinding.inflate(layoutInflater)
        binding.newsArticle = intent.extras?.getSerializable("newsArticle") as NewsArticle
        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}