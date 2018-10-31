package com.kotlin.base.injection.component

import android.content.Context
import dagger.Component
import com.kotlin.base.injection.moudle.AppMoudle
import javax.inject.Singleton

/**
 * Description:
 * Author: devil
 * Date: 2018/8/23
 */
@Singleton
@Component(modules = arrayOf(AppMoudle::class))
interface AppComponent {
    fun context():Context
}