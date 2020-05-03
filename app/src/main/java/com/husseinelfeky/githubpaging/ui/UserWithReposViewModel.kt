package com.husseinelfeky.githubpaging.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.husseinelfeky.githubpaging.common.utils.NetworkState
import com.husseinelfeky.githubpaging.persistence.entities.UserWithRepos
import com.husseinelfeky.githubpaging.repository.userwithrepos.UserWithReposRepository
import io.reactivex.Flowable

class UserWithReposViewModel(private val fetchingRepo: UserWithReposRepository) : ViewModel() {

    val networkState = MutableLiveData<NetworkState>()
    val refreshState = MutableLiveData<NetworkState>()

    fun getUsersWithRepos(page: Int): Flowable<List<UserWithRepos>> {
        return fetchingRepo.getUsersWithRepos(page)
    }

    fun invalidateDataSource() {
        // TODO: Implement refresh logic.
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val fetchingRepo: UserWithReposRepository) : ViewModelProvider.Factory {
        override fun <VM : ViewModel?> create(modelClass: Class<VM>): VM =
            UserWithReposViewModel(fetchingRepo) as VM
    }
}
