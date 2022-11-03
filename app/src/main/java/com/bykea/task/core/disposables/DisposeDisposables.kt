package com.bykea.task.core.disposables

import io.reactivex.disposables.Disposable
import javax.inject.Inject

class DisposeDisposables @Inject constructor() : Dispose{

    private val disposables: MutableList<Disposable?> = mutableListOf()

    fun add(disposable: Disposable?){
        disposables.add(disposable)
    }

    override fun dispose() {
        disposables.forEach{
            if(it?.isDisposed == false){
                it.dispose()
            }
        }
        disposables.clear()
    }
}