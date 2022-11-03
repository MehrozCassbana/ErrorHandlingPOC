package com.bykea.task.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment

abstract class BaseDialogFragment<VM : BaseViewModel, DB : ViewDataBinding> : DialogFragment() {


    lateinit var mView: View

    /**
     * @return [Int] get id for the layout resource
     */
    @LayoutRes
    abstract fun getLayoutRes(): Int


    /**
     * @return view model factory
     */

    open lateinit var binding: DB

    private fun init(inflater: LayoutInflater, container: ViewGroup) {
        binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
    }

    open fun initViewModel(bundle: Bundle?){}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        init(inflater, container!!)
        super.onCreateView(inflater, container, savedInstanceState)
        initViewModel(savedInstanceState)
        return binding.root
    }

}