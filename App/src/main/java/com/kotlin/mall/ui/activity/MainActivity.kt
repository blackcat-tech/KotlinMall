package com.kotlin.mall.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.kotlin.base.ui.activity.BaseActivity
import com.kotlin.goods.ui.fragment.CategoryFragment
import com.kotlin.mall.R
import com.kotlin.mall.ui.fragment.HomeFragment
import com.kotlin.mall.ui.fragment.MeFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : BaseActivity() {


    //Fragment 栈管理
    private val mStack = Stack<Fragment>()
    //主界面Fragment
    private val mHomeFragment: HomeFragment by lazy { HomeFragment.instance("fragment") }
    //商品分类Fragment
    private val mCategoryFragment by lazy { CategoryFragment() }
    //购物车Fragment
    private val mCartFragment by lazy { HomeFragment.instance("fragment") }
    //消息Fragment
    private val mMsgFragment by lazy { HomeFragment.instance("fragment") }
    //"我的"Fragment
    private val mMeFragment by lazy { MeFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFragment()
        initBottomNav()
        changeFragment(0)
    }

    private fun initFragment() {
//        mHomeFragment = HomeFragment.instance("fragment")
//        val manager = supportFragmentManager.beginTransaction()
//        manager.add(R.id.mContaier, mHomeFragment)
//        manager.commitAllowingStateLoss()//不建议用commit，可能会出现一些错误

        with(supportFragmentManager.beginTransaction()) {
            add(R.id.mContaier, mHomeFragment)
            add(R.id.mContaier, mCategoryFragment)
            add(R.id.mContaier, mCartFragment)
            add(R.id.mContaier, mMsgFragment)
            add(R.id.mContaier, mMeFragment)
            commit()
        }

        with(mStack) {
            add(mHomeFragment)
            add(mCategoryFragment)
            add(mCartFragment)
            add(mMsgFragment)
            add(mMeFragment)
        }
    }

    /*
        初始化底部导航切换事件
     */
    private fun initBottomNav(){
        mBottomNavBar.setTabSelectedListener(object : BottomNavigationBar.OnTabSelectedListener{
            override fun onTabReselected(position: Int) {
            }

            override fun onTabUnselected(position: Int) {
            }

            override fun onTabSelected(position: Int) {
                changeFragment(position)
            }
        })

        mBottomNavBar.checkMsgBadge(false)
    }

    /*
        切换Tab，切换对应的Fragment
     */
    private fun changeFragment(position: Int) {
        val manager = supportFragmentManager.beginTransaction()
        for (fragment in mStack){
            manager.hide(fragment)
        }

        manager.show(mStack[position])
        manager.commit()
    }

}
