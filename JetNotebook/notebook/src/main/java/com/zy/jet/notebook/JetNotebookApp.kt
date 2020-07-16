package com.zy.jet.notebook

import androidx.multidex.MultiDex
import cn.charles.kasa.framework.base.BaseApp
import com.zy.jet.notebook.jetpack.LOG_TAG

class JetNotebookApp : BaseApp() {
    override fun getLogTagName(): String = "jetnotebook"


    override fun onCreate() {
        super.onCreate()

        // 主要是添加下面这句代码
        MultiDex.install(this);
        LOG_TAG = logTagName
    }

}