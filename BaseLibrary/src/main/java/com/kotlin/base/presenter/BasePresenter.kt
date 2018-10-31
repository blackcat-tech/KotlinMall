package com.kotlin.base.presenter

import android.content.Context
import com.kotlin.base.presenter.view.BaseView
import com.kotlin.base.utils.NetWorkUtils
import javax.inject.Inject

open class BasePresenter<T: BaseView> {
    lateinit var mView: T


//    @Inject
//    lateinit var context: Context
    /*
        检查网络是否可用
     */
    fun checkNetWork(context:Context):Boolean{
        if(NetWorkUtils.isNetWorkAvailable(context)){
            return true
        }
        mView.onError("网络不可用")
        return false
    }
}