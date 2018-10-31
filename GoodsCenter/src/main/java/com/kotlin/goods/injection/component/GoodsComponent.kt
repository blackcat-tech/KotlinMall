package com.kotlin.goods.injection.component

import com.kotlin.goods.injection.module.CartModule
import com.kotlin.goods.injection.module.GoodsModule
import com.kotlin.goods.ui.activity.GoodsActivity
import com.kotlin.goods.ui.fragment.GoodsDetailTabOneFragment
import dagger.Component

/*
    商品Component
 */
@Component(modules = arrayOf(GoodsModule::class,CartModule::class))
interface GoodsComponent {
    fun inject(activity: GoodsActivity)
    fun inject(fragment: GoodsDetailTabOneFragment)
}
