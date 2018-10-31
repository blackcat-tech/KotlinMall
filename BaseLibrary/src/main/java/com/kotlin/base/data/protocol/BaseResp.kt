package com.kotlin.base.data.protocol

/**
 * Description:
 * Author: devil
 * Date: 2018/8/22
 */
class BaseResp<T>(val status:Int,val message:String,val data:T) {
}