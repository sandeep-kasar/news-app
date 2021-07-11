package com.example.newsapp.ui.main.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.data.model.NewsArticle
import com.example.newsapp.databinding.RowNewsArticleBinding
import com.example.newsapp.ui.main.view.NewsArticleDetail

/**
 * This is the main adapter, to display news article data
 * data binding is used
 *
 * @author SandeepK
 * @version 1.0
 * */
class MainAdapter(
    private val newsArticle: ArrayList<NewsArticle>,
) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    inner class MainViewHolder(
        val rowNewsArticleBinding: RowNewsArticleBinding,
    ): RecyclerView.ViewHolder(rowNewsArticleBinding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MainViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_news_article,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = newsArticle.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        // get particular rows data
        holder.rowNewsArticleBinding.newsArticle = newsArticle[position]

        // onclick row, open NewsArticleDetail activity
        holder.rowNewsArticleBinding.root.setOnClickListener {
            val context = holder.rowNewsArticleBinding.root.context
            val intent = Intent(context, NewsArticleDetail::class.java).apply {
                val bundle = Bundle()
                bundle.putSerializable("newsArticle", newsArticle[position])
                putExtras(bundle)
            }
            context.startActivity(intent)
        }
    }

    /**
     * save news articles which we have received from API call
     * */
    fun addNewsArticle(newsArticle: List<NewsArticle>) {
        this.newsArticle.apply {
            clear()
            addAll(newsArticle)
        }

    }
}