package com.husseinelfeky.githubpaging.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.husseinelfeky.githubpaging.common.NetworkState
import com.husseinelfeky.githubpaging.persistence.entities.UserWithRepos
import com.husseinelfeky.githubpaging.repository.ReposRepository

class ReposViewModel(private val reposRepository: ReposRepository) : ViewModel() {

    val usersWithReposPagedList: LiveData<PagedList<UserWithRepos>>

    val networkState = MutableLiveData<NetworkState>()
    val refreshState = MutableLiveData<NetworkState>()

    init {
        val pagedListConfig = Config(
            pageSize = PAGE_SIZE,
            enablePlaceholders = PLACEHOLDERS_ENABLED
        )
        usersWithReposPagedList = reposRepository.getUsersWithRepos().toLiveData(pagedListConfig)
    }

    fun invalidateDataSource() {
        usersWithReposPagedList.value?.dataSource?.invalidate()
    }

    companion object {
        private const val PAGE_SIZE = 2
        private const val PLACEHOLDERS_ENABLED = false
    }
}
