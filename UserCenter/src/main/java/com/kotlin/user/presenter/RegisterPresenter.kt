package com.kotlin.user.presenter

import android.content.Context
import android.util.Log
import com.kotlin.base.ext.execute
import com.kotlin.base.presenter.BasePresenter
import com.kotlin.base.rx.BaseSubscriber
import com.kotlin.user.presenter.view.RegisterView
import com.kotlin.user.service.UserService
import javax.inject.Inject
import javax.inject.Named

open class RegisterPresenter @Inject constructor(): BasePresenter<RegisterView>(){

    @Inject
    @field:[Named("userService")]
    lateinit var userServiceImpl : UserService

    fun register(context:Context,mobile:String,pwd:String,verifyCode:String){

        var isConnectNetWrok = checkNetWork(context)
        if (!isConnectNetWrok){
            return
        }
        mView.showLoading()
        //被订阅者
        userServiceImpl.register(mobile,pwd,verifyCode)
                .execute(object : BaseSubscriber<Boolean>(mView){
                    override fun onNext(t: Boolean) {
                        if (t){
                            mView.onRegisterResult("注册成功")
                        }
                    }
                })

    }

}