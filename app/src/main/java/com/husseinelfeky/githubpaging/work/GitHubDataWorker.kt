package com.husseinelfeky.githubpaging.work

import android.content.Context
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.husseinelfeky.githubpaging.persistence.AppRoomDatabase.Companion.getDatabase
import com.husseinelfeky.githubpaging.persistence.entities.GitHubRepo
import com.husseinelfeky.githubpaging.persistence.entities.User
import com.husseinelfeky.githubpaging.repository.ReposRepository
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import timber.log.Timber

class GitHubDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = ReposRepository(database.gitHubDao())

        try {
            repository.getUsersRemotely().first().forEach { user ->
                repository.saveUser(
                    User(
                        user.id,
                        user.userName,
                        user.avatarUrl
                    )
                )
                repository.getRepositoriesRemotely(user.userName).first().forEach { repo ->
                    repository.saveRepo(
                        GitHubRepo(
                            repo.id,
                            user.id,
                            repo.name
                        )
                    )
                }
            }

            Toast.makeText(
                applicationContext,
                "Fetching users from the internet",
                Toast.LENGTH_SHORT
            ).show()

            Timber.d("WorkManager: Work request for syncing users and repos is run.")
        } catch (e: HttpException) {
            return Result.retry()
        }

        return Result.success()
    }

    companion object {
        const val WORK_NAME = "GitHubDataWorker"
    }
}
