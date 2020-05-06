package com.husseinelfeky.githubpaging.common.paging.utils

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.addToCompositeDisposable(compositeDisposable: CompositeDisposable): Disposable {
    compositeDisposable.add(this)
    return this
}
