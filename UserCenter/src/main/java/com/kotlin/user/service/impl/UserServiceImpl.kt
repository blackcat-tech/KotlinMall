package com.kotlin.user.service.impl

import com.kotlin.base.rx.BaseException
import com.kotlin.base.rx.BaseFuncBoolean
import com.kotlin.user.data.respository.UserRespository
import com.kotlin.user.service.UserService
import io.reactivex.Observable
import io.reactivex.functions.Function
import com.kotlin.base.data.protocol.BaseResp
import com.kotlin.base.ext.convert
import com.kotlin.base.ext.convertBoolean
import com.kotlin.user.data.protocol.UserInfo
import javax.inject.Inject

class UserServiceImpl @Inject constructor():UserService{

    @Inject
    lateinit var respository:UserRespository

    /**
     * 注册
     */
    override fun register(mobile: String, pwd: String, verifyCode: String): Observable<Boolean> {

        return respository.register(mobile,pwd,verifyCode)
                .convertBoolean()
    }

    /**
     * 登录
     */
    override fun login(mobile: String, pwd: String, pushId: String): Observable<UserInfo> {
        return respository.login(mobile,pwd,pushId).convert()
    }

    override fun editUser(userIcon: String, userName: String, userGender: String, userSign: String): Observable<UserInfo> {
        return respository.editUser(userIcon,userName,userGender,userSign).convert()
    }

}