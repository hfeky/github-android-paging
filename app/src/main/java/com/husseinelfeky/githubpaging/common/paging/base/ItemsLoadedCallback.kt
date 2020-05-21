package com.husseinelfeky.githubpaging.common.paging.base

abstract class ItemsLoadedCallback<Entity : Any> : (List<Entity>) -> Unit
