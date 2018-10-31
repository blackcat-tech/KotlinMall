package com.kotlin.user.data.api

import com.kotlin.user.data.protocol.RegisterReq
import io.reactivex.Observable
import com.kotlin.base.data.protocol.BaseResp
import com.kotlin.user.data.protocol.EditUserReq
import com.kotlin.user.data.protocol.LoginReq
import com.kotlin.user.data.protocol.UserInfo
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Description:网络请求API
 * Author: devil
 * Date: 2018/8/22
 */
interface UserApi {
    /**
     * 用户注册
     */
    @POST("userCenter/register")
    fun register(@Body req: RegisterReq): Observable<BaseResp<String>>

    /*
        用户登录
     */
    @POST("userCenter/login")
    fun login(@Body req: LoginReq):Observable<BaseResp<UserInfo>>
//
//    /*
//        忘记密码
//     */
//    @POST("userCenter/forgetPwd")
//    fun forgetPwd(@Body req:ForgetPwdReq):Observable<BaseResp<String>>
//
//    /*
//        重置密码
//     */
//    @POST("userCenter/resetPwd")
//    fun resetPwd(@Body req:ResetPwdReq):Observable<BaseResp<String>>
//
    /*
        编辑用户资料
     */
    @POST("userCenter/editUser")
    fun editUser(@Body req: EditUserReq):Observable<BaseResp<UserInfo>>
}