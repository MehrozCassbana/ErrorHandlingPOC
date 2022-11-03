package com.bykea.task.core.comparator

interface ItemComparator<T> {
    fun areItemsTheSame(oldItem: T, newItem: T): Boolean
    fun areContentsTheSame(oldItem: T, newItem: T): Boolean
    fun getChangePayload(oldItem: T, newItem: T): Any?
}