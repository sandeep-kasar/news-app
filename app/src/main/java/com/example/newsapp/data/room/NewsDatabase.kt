package com.example.newsapp.data.room

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [NewsArticleDb::class], version = 1)
abstract class NewsDatabase : RoomDatabase() {

    /**
     * Get news article DAO
     */
    abstract fun newsArticlesDao(): NewsArticlesDao

    companion object {

        private const val databaseName = "news-db"

        fun buildDefault(context: Context) =
            Room.databaseBuilder(context, NewsDatabase::class.java, databaseName)
                .build()

        @VisibleForTesting
        fun buildTest(context: Context) =
            Room.inMemoryDatabaseBuilder(context, NewsDatabase::class.java)
                .build()
    }
}
