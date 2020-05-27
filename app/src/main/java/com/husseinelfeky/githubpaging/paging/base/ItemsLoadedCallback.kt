package com.husseinelfeky.githubpaging.paging.base

abstract class ItemsLoadedCallback<Entity : Any> : (List<Entity>) -> Unit
