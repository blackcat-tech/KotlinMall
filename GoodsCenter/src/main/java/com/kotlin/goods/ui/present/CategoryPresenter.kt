package com.kotlin.goods.presenter

import android.content.Context
import com.kotlin.base.ext.execute
import com.kotlin.base.presenter.BasePresenter
import com.kotlin.base.rx.BaseSubscriber
import com.kotlin.goods.data.protocol.Category
import com.kotlin.goods.presenter.view.CategoryView
import com.kotlin.goods.service.CategoryService
import javax.inject.Inject

/*
    商品分类 Presenter
 */
class CategoryPresenter @Inject constructor() : BasePresenter<CategoryView>() {

    @Inject
    lateinit var categoryService: CategoryService


    /*
        获取商品分类列表
     */
    fun getCategory(context: Context,parentId:Int) {
        if (!checkNetWork(context)) {
            return
        }
        mView.showLoading()
        categoryService.getCategory(parentId).execute(object : BaseSubscriber<MutableList<Category>?>(mView) {
            override fun onNext(t: MutableList<Category>?) {
                    mView.onGetCategoryResult(t)
            }
        })

    }

}
