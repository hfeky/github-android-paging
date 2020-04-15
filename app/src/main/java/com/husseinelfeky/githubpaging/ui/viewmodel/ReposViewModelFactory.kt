package com.husseinelfeky.githubpaging.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.husseinelfeky.githubpaging.repository.ReposRepository

class ReposViewModelFactory(
    private val reposRepository: ReposRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ReposViewModel(reposRepository) as T
    }
}
