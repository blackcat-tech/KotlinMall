package com.kotlin.goods.presenter

import android.content.Context
import com.kotlin.base.ext.execute
import com.kotlin.base.presenter.BasePresenter
import com.kotlin.base.rx.BaseSubscriber
import com.kotlin.goods.data.protocol.Goods
import com.kotlin.goods.presenter.view.GoodsListView
import com.kotlin.goods.service.GoodsService
import javax.inject.Inject

/*
    商品列表 Presenter
 */
class GoodsListPresenter @Inject constructor() : BasePresenter<GoodsListView>() {

    @Inject
    lateinit var goodsService: GoodsService


    /*
        获取商品列表
     */
    fun getGoodsList(context: Context,categoryId: Int, pageNo: Int) {
        if (!checkNetWork(context)) {
            return
        }
        mView.showLoading()

        goodsService.getGoodsList(categoryId,pageNo).execute(object : BaseSubscriber<MutableList<Goods>?>(mView) {
            override fun onNext(t: MutableList<Goods>?) {
                mView.onGetGoodsListResult(t)
            }
        })

    }

    /*
        根据关键字 搜索商品
     */
    fun getGoodsListByKeyword(context: Context,keyword: String, pageNo: Int) {
        if (!checkNetWork(context)) {
            return
        }
        mView.showLoading()
        goodsService.getGoodsListByKeyword(keyword,pageNo).execute(object : BaseSubscriber<MutableList<Goods>?>(mView) {
            override fun onNext(t: MutableList<Goods>?) {
                mView.onGetGoodsListResult(t)
            }
        })

    }

}
