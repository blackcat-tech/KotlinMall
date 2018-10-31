package com.kotlin.base.common

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter

/**
 * Description:
 * Author: devil
 * Date: 2018/8/23
 */
class BaseApplication : Application() {

    /*
        全局伴生对象
     */
    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this

        initARouter()
    }

    private fun initARouter() {
        //调试
        ARouter.openLog()
        ARouter.openDebug()

        ARouter.init(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}