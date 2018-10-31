package com.kotlin.user.presenter

import android.content.Context
import android.util.Log
import com.kotlin.base.ext.execute
import com.kotlin.base.presenter.BasePresenter
import com.kotlin.base.rx.BaseSubscriber
import com.kotlin.user.data.protocol.UserInfo
import com.kotlin.user.presenter.view.LoginView
import com.kotlin.user.presenter.view.RegisterView
import com.kotlin.user.service.UserService
import javax.inject.Inject
import javax.inject.Named

open class LoginPresenter @Inject constructor() : BasePresenter<LoginView>() {

    @Inject
    @field:[Named("userService")]
    lateinit var userServiceImpl: UserService

    fun login(context: Context, mobile: String, pwd: String, pushId: String) {

        var isConnectNetWrok = checkNetWork(context)
        if (!isConnectNetWrok) {
            return
        }
        mView.showLoading()
        //被订阅者
        userServiceImpl.login(mobile, pwd, pushId)
                .execute(object : BaseSubscriber<UserInfo>(mView) {
                    override fun onNext(t: UserInfo) {
                        mView.onLoginResult(t)
                    }
                })

    }

}