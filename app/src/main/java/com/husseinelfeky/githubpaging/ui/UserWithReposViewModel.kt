package com.husseinelfeky.githubpaging.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.husseinelfeky.githubpaging.common.utils.NetworkState
import com.husseinelfeky.githubpaging.repository.userwithrepos.UserWithReposRepository

class UserWithReposViewModel(
    private val userWithReposRepository: UserWithReposRepository
) : ViewModel() {

    val networkState = MutableLiveData<NetworkState>()
    val refreshState = MutableLiveData<NetworkState>()

    fun invalidateDataSource() {
//        usersWithReposPagedList.value?.dataSource?.invalidate()
    }

    companion object {
        private const val PAGE_SIZE = 2
    }
}
