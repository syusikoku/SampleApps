package com.zy.jet.notebook.jetpack.viewmodel

import android.text.TextUtils
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import cn.charles.kasa.framework.utils.ResourceUtils
import com.zy.jet.notebook.R

/**
 * 首页的viewmodel
 */
class MainViewModel : ViewModel() {
    val defTitle: String = ResourceUtils.getStr(R.string.app_name);

    // 标题数据
    val titleData: ObservableField<String> =
        ObservableField(defTitle)

    /**
     * 是否在webview界面
     */
    val isInMainUI: ObservableBoolean = ObservableBoolean(true)


    /**
     * 设置标题
     */
    fun chageTitle(title: String) {
        var newTitle = title
        if (TextUtils.isEmpty(title)) {
            newTitle = defTitle
        }
        titleData.set(newTitle)
    }

    /**
     * 是否是在主界面
     */
    fun setInMainUI(b: Boolean) {
        isInMainUI.set(b)
    }
}