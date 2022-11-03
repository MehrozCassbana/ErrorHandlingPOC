package com.bykea.task.core.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bykea.task.core.adapter.diffutil.DiffUtilCallback
import com.bykea.task.core.adapter.viewholder.BaseViewHolder
import com.bykea.task.core.comparator.BaseItemComparator
import com.bykea.task.core.comparator.ItemComparator
import com.bykea.task.core.disposables.DisposeDisposables
import com.bykea.task.core.extensions.addDisposable

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class BaseAdapter<T>(
    val dataItems: ArrayList<T> = ArrayList(),
    private val comparator: ItemComparator<T> = BaseItemComparator(),
    private val listener: (item: T) -> Unit = {}
) :
    RecyclerView.Adapter<BaseViewHolder<T>>() {

    private val disposables: DisposeDisposables = DisposeDisposables()

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T>

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.onBind(position, dataItems[position], listener)
    }

    override fun getItemCount(): Int {
        return dataItems.size
    }

    fun onItemRemoved(position: Int) {
        dataItems.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount);
    }

    fun update(items: List<T>) {
        if (dataItems.isEmpty()) {
            updateAllItems(items)
        } else {
            updateDiffItemsOnly(items)
        }
    }

    private fun updateAllItems(items: List<T>) {
        Single.just(items)
            .doOnSuccess { data: List<T> ->
                updateItemsInModel(data)
            }
            .doOnSuccess {
                notifyDataSetChanged()
            }
            .subscribe({},{}).addDisposable(disposables)
    }

    private fun updateDiffItemsOnly(items: List<T>) {
        Single.fromCallable { calculateDiff(items) }
            .subscribeOn(Schedulers.io())
            .doOnSuccess { updateItemsInModel(items) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::updateAdapterWithDiffResult) {}.addDisposable(disposables)
    }

    private fun calculateDiff(newItems: List<T>): DiffUtil.DiffResult {
        return DiffUtil.calculateDiff(DiffUtilCallback(dataItems, newItems, comparator))
    }

    private fun updateItemsInModel(items: List<T>) {
        dataItems.clear()
        dataItems.addAll(items)
    }

    private fun updateAdapterWithDiffResult(result: DiffUtil.DiffResult) {
        result.dispatchUpdatesTo(this)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        disposables.dispose()
    }
}