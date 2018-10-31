package com.kotlin.user.presenter

import android.content.Context
import com.kotlin.base.ext.execute
import com.kotlin.base.presenter.BasePresenter
import com.kotlin.base.rx.BaseSubscriber
import com.kotlin.user.data.protocol.UserInfo
import com.kotlin.user.presenter.view.UserInfoView
import com.kotlin.user.service.UploadService
import com.kotlin.user.service.UserService
import javax.inject.Inject
import javax.inject.Named

open class UserInfoPresenter @Inject constructor() : BasePresenter<UserInfoView>() {

    @Inject
    @field:[Named("userService")]
    lateinit var userServiceImpl: UserService

    @Inject
    lateinit var uploadService: UploadService

    /*
        获取七牛云上传凭证
     */
    fun getUploadToken(context: Context){
        if (!checkNetWork(context))
            return

        mView.showLoading()
        uploadService.getUploadToken().execute(object : BaseSubscriber<String>(mView){
            override fun onNext(t: String) {
                mView.onGetUploadTokenResult(t)
            }
        })
    }

    /*
        编辑用户资料
     */
    fun editUser(context: Context,userIcon:String,userName:String,userGender:String,userSign:String){
        if (!checkNetWork(context))
            return

        mView.showLoading()
        userServiceImpl.editUser(userIcon,userName,userGender,userSign).execute(object :BaseSubscriber<UserInfo>(mView){
            override fun onNext(t: UserInfo) {
                mView.onEditUserResult(t)
            }
        })
    }



}