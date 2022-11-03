package com.bykea.task.core.comparator

class BaseItemComparator<T> : ItemComparator<T> {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: T, newItem: T): Any? {
        return null
    }
}