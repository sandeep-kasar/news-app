package com.example.newsapp.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.data.model.NewsArticle
import com.example.newsapp.databinding.RowNewsArticleBinding

class MainAdapter(
    private val newsArticle: ArrayList<NewsArticle>
) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    inner class MainViewHolder(
        val rowNewsArticleBinding: RowNewsArticleBinding
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
        holder.rowNewsArticleBinding.newsArticle = newsArticle[position]
        holder.rowNewsArticleBinding.root.setOnClickListener {
            Toast.makeText(holder.rowNewsArticleBinding.root.context,"Click !!!", Toast.LENGTH_LONG).show()
        }
    }

    fun addNewsArticle(newsArticle: List<NewsArticle>) {
        this.newsArticle.apply {
            clear()
            addAll(newsArticle)
        }

    }
}