package com.kotlin.user.injection.component

import com.kotlin.user.injection.module.UserModule
import com.kotlin.user.ui.activity.RegisterActivity
import dagger.Component
import com.kotlin.base.ui.activity.BaseMvpActivity
import com.kotlin.user.injection.module.UploadModule
import com.kotlin.user.ui.activity.LoginActivity
import com.kotlin.user.ui.activity.UserInfoActivity

/**
 * Description:
 * Author: devil
 * Date: 2018/8/22
 */
@Component(modules = arrayOf(UserModule::class,UploadModule::class))
interface UserComponent {
    fun inject(activity: RegisterActivity)
    fun inject(activity: LoginActivity)
    fun inject(activity: UserInfoActivity)
}