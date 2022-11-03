package com.bykea.task.core.extensions

import com.bykea.task.core.disposables.DisposeDisposables
import io.reactivex.disposables.Disposable

fun Disposable.addDisposable(disposable: DisposeDisposables) {
    disposable.add(this)
}