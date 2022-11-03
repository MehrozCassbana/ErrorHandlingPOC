package com.bykea.task.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding>(private val mViewModelClass: Class<VM>) :
    Fragment() {

    /**
     * @return [Int] get id for the layout resource
     */
    @LayoutRes
    abstract fun getLayoutRes(): Int


    open lateinit var binding: DB

    private fun init(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        init(inflater, container)
        super.onCreateView(inflater, container, savedInstanceState)
        initialize()
        binding.lifecycleOwner = this
        return binding.root
    }

    abstract fun initialize()
}



