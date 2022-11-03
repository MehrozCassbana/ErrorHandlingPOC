package com.bykea.task.core.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.bykea.task.core.comparator.ItemComparator

internal class DiffUtilCallback<T>(
    private val oldItems: List<T>,
    private val newItems: List<T>,
    private val comparator: ItemComparator<T>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return comparator.areItemsTheSame(
            oldItems[oldItemPosition],
            newItems[newItemPosition]
        )
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if(oldItems.isEmpty() || newItems.isEmpty()){
            return false
        }
        return comparator.areContentsTheSame(
            oldItems[oldItemPosition],
            newItems[newItemPosition]
        )
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return comparator.getChangePayload(oldItems[oldItemPosition], newItems[newItemPosition])
    }
}