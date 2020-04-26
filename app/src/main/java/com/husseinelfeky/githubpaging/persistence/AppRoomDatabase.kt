package com.husseinelfeky.githubpaging.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.husseinelfeky.githubpaging.GitHubPagingApp
import com.husseinelfeky.githubpaging.persistence.dao.GitHubDao
import com.husseinelfeky.githubpaging.persistence.entities.GitHubRepo
import com.husseinelfeky.githubpaging.persistence.entities.User

@Database(entities = [User::class, GitHubRepo::class], version = 1, exportSchema = false)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun gitHubDao(): GitHubDao

    companion object {
        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        fun getDatabase(context: Context): AppRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }

        fun getDatabase(): AppRoomDatabase {
            return getDatabase(GitHubPagingApp.instance)
        }
    }
}
