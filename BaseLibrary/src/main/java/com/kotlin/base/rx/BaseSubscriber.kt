package com.kotlin.base.rx

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import com.kotlin.base.presenter.view.BaseView

/**
 * Rx订阅者默认实现
 */
open class BaseSubscriber<T>(val baseView:BaseView):Observer<T> {

    override fun onComplete() {
        baseView.hideLoading()
    }

    override fun onSubscribe(d: Disposable) {

    }

    override fun onNext(t: T) {

    }

    override fun onError(e: Throwable) {
        baseView.hideLoading()
        if (e is BaseException){
            baseView.onError(e.msg)
        }
    }

}