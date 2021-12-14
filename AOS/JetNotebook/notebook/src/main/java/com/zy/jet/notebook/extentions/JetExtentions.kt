package com.zy.jet.notebook.extentions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.zy.jet.notebook.jetpack.viewmodel.factory.KasaViewModelFactory

/**
 *  复用ViewModel
 */
fun <T : ViewModel> Fragment.obtainViewModel(viewModelCls: Class<T>) =
    ViewModelProviders.of(this, activity?.application?.let {
        KasaViewModelFactory.getInstance(it)
    }).get(viewModelCls)

/**
 *  复用ViewModel
 */
fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelCls: Class<T>) =
    ViewModelProviders.of(this, application?.let {
        KasaViewModelFactory.getInstance(it)
    }).get(viewModelCls)

////////////////////////// DataBinding //////////////////////////