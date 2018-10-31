package com.kotlin.base.widgets

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.kotlin.base.ext.onClick
import kotlinx.android.synthetic.main.layout_header_bar.view.*
import mall.kotlin.base.R

/**
 * Description:HeaderBar封装类(返回、标题文字、右边文字)
 * Author: devil
 * Date: 2018/9/2
 */
class HeaderBar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var isShowBack: Boolean = true
    var titleText: String? = null
    var rightText: String? = null

    init {
        var typeArray = context.obtainStyledAttributes(attrs, R.styleable.HeaderBar)
        isShowBack = typeArray.getBoolean(R.styleable.HeaderBar_isShowBack, true)
        titleText = typeArray.getString(R.styleable.HeaderBar_titleText)
        rightText = typeArray.getString(R.styleable.HeaderBar_rightText)
        //回收，防止内存泄漏
        typeArray.recycle()

        initView()
    }

    private fun initView() {
        View.inflate(context, R.layout.layout_header_bar, this)
        mLeftIv.visibility = if (isShowBack) View.VISIBLE else View.GONE
        titleText?.let {
            mTitleTv.text = it
        }
        rightText?.let {
            mRightTv.text = it
            mRightTv.visibility = View.VISIBLE
        }
        mLeftIv.onClick {
            if (context is Activity){
                (context as Activity).finish()
            }
        }
    }

    fun getRightView(): TextView {
        return mRightTv
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }


}