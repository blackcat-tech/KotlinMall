package com.kotlin.base.ui.activity

import android.Manifest
import android.os.Bundle
import com.kotlin.base.lisenter.PermissionLisenter
import com.kotlin.base.presenter.BasePresenter
import com.kotlin.base.presenter.view.BaseView
import com.kotlin.base.widgets.ProgressLoading
import org.jetbrains.anko.toast
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

abstract class BaseMvpActivity<T: BasePresenter<*>>: BaseActivity(), BaseView , EasyPermissions.PermissionCallbacks {

    override fun showLoading() {
        mLoadingDialog.showLoading()
    }

    override fun hideLoading() {
        mLoadingDialog.hideLoading()
    }

    override fun onError(error: String) {
        toast(error)
    }

    @Inject
    lateinit var mPresenter: T

    private lateinit var mLoadingDialog: ProgressLoading

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()

        //初始加载框
        mLoadingDialog = ProgressLoading.create(this)
    }

    /**
     * 在实现Activity中初始化Dagger
     */
    abstract fun injectComponent()

    /**
     * 回调时机：用户点了不在询问并拒绝，会弹出此对话框。而且之后，我们每次检测权限时，都会弹出，引导用户去系统设置开启权限。（Google建议）
     */
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
            //也可以自定义对话框
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        permissionsGranted(requestCode,perms)
    }

    abstract fun permissionsGranted(requestCode: Int, perms: MutableList<String>)

    fun checkPermissions(rational: String,requestCode: Int,perms: Array<out String>,permissionLisenter: PermissionLisenter) {
        if (EasyPermissions.hasPermissions(this, *perms)) {
            toast("权限已申请")
            permissionLisenter.onPermissionsGranted()
        } else {// 没有申请过权限，提示去申请
            EasyPermissions.requestPermissions(this, rational, requestCode, *perms)
        }
    }
}