package com.husseinelfeky.githubpaging.repository.userwithrepos

import com.husseinelfeky.githubpaging.common.paging.datasource.paged.PagedDataSource
import com.husseinelfeky.githubpaging.common.paging.datasource.common.CachingLayer
import com.husseinelfeky.githubpaging.persistence.entities.User
import io.reactivex.Completable

class UsersFetchingRepo : PagedDataSource<User>(), CachingLayer {

    private val db = UserWithReposDataSource.gitHubDao

    override fun getPageSize(): Int = 2

    /**
     * @see com.husseinelfeky.githubpaging.api.GitHubApi.getUsers to understand
     * how pagination works for GitHub users.
     */
    override fun fetchItemsFromNetwork(page: Int, vararg params: Any) =
        UserWithReposDataSource.gitHubApi.getUsers(getOffset(page), getPageSize())

    override fun fetchItemsFromDatabase(page: Int, vararg params: Any) =
        db.getUsers(getPageSize(), getOffset(page))

    override fun saveItemsToDatabase(itemsList: List<Any>): Completable =
        db.insertUsers(itemsList as List<User>)
}
