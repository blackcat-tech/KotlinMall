package com.kotlin.user.data.respository

import com.kotlin.user.data.api.UserApi
import com.kotlin.user.data.protocol.RegisterReq
import io.reactivex.Observable
import com.kotlin.base.data.net.RetrofitFactory
import com.kotlin.base.data.protocol.BaseResp
import com.kotlin.user.data.protocol.EditUserReq
import com.kotlin.user.data.protocol.LoginReq
import com.kotlin.user.data.protocol.UserInfo
import javax.inject.Inject

/**
 * Description:网络请求
 * Author: devil
 * Date: 2018/8/22
 */

class UserRespository @Inject constructor() {
    /**
     * 用户注册
     */
    fun register(mobile:String,pwd:String,verifyCode:String):Observable<BaseResp<String>>{
        return  RetrofitFactory.instance.create(UserApi::class.java)
                .register(RegisterReq(mobile,pwd,verifyCode))
    }
    /**
     * 登录
     */
    fun login(mobile:String,pwd:String,pushId:String):Observable<BaseResp<UserInfo>>{
        return  RetrofitFactory.instance.create(UserApi::class.java)
                .login(LoginReq(mobile,pwd,pushId))
    }

    /*
        编辑用户资料
     */
    fun editUser(userIcon:String,userName:String,userGender:String,userSign:String):Observable<BaseResp<UserInfo>>{
        return RetrofitFactory.instance.create(UserApi::class.java).editUser(EditUserReq(userIcon,userName,userGender,userSign))
    }
}