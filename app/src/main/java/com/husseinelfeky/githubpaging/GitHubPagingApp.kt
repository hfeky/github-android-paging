package com.husseinelfeky.githubpaging

import android.app.Application
import androidx.work.*
import com.husseinelfeky.githubpaging.work.GitHubDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

class GitHubPagingApp : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        delayedInit()
    }

    private fun delayedInit() {
        applicationScope.launch {
            initGitHubWorker()
        }
    }

    private fun initGitHubWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()

        val gitHubWorkRequest =
            PeriodicWorkRequestBuilder<GitHubDataWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            GitHubDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            gitHubWorkRequest
        )
    }
}
