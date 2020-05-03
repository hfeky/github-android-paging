package com.husseinelfeky.githubpaging

import android.app.Application
import timber.log.Timber

class GitHubPagingApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        instance = this
    }

    companion object {
        lateinit var instance: GitHubPagingApp
    }
}
