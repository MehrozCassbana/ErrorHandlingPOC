package com.bykea.task.core.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun onBind(position: Int, data: T, listener: (data: T) -> Unit)
}