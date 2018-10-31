package com.kotlin.base.injection.moudle

import android.content.Context
import dagger.Module
import dagger.Provides
import com.kotlin.base.common.BaseApplication
import javax.inject.Singleton

/**
 * Description:
 * Author: devil
 * Date: 2018/8/23
 */
@Module
class AppMoudle (private val context: BaseApplication){

    @Provides
    @Singleton
    fun providersContext(): Context{
        return context
    }
}