package com.kotlin.user.ui.activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.bigkoo.alertview.AlertView
import com.bigkoo.alertview.OnItemClickListener
import com.jph.takephoto.app.TakePhoto
import com.jph.takephoto.app.TakePhotoImpl
import com.jph.takephoto.compress.CompressConfig
import com.jph.takephoto.model.InvokeParam
import com.jph.takephoto.model.TContextWrap
import com.jph.takephoto.model.TResult
import com.jph.takephoto.permission.InvokeListener
import com.jph.takephoto.permission.PermissionManager
import com.jph.takephoto.permission.PermissionManager.TPermissionType
import com.kotlin.base.common.BaseConstant
import com.kotlin.base.ext.onClick
import com.kotlin.base.lisenter.PermissionLisenter
import com.kotlin.base.ui.activity.BaseMvpActivity
import com.kotlin.base.utils.AppPrefsUtils
import com.kotlin.base.utils.DateUtils
import com.kotlin.base.utils.GlideUtils
import com.kotlin.provider.common.ProviderConstant
import com.kotlin.user.R
import com.kotlin.user.data.protocol.UserInfo
import com.kotlin.user.injection.component.DaggerUserComponent
import com.kotlin.user.injection.module.UserModule
import com.kotlin.user.presenter.UserInfoPresenter
import com.kotlin.user.presenter.view.UserInfoView
import com.kotlin.user.utils.UserPrefsUtils
import com.qiniu.android.http.ResponseInfo
import com.qiniu.android.storage.UpCompletionHandler
import com.qiniu.android.storage.UploadManager
import kotlinx.android.synthetic.main.activity_user_info.*
import org.jetbrains.anko.toast
import org.json.JSONObject
import pub.devrel.easypermissions.EasyPermissions
import java.io.File


/*
    用户信息
 */
class UserInfoActivity : BaseMvpActivity<UserInfoPresenter>(), UserInfoView, TakePhoto.TakeResultListener, InvokeListener {

    companion object {
        const val TAKE_PHOTO_ITEM_CAPTURE = 0
        const val TAKE_PHOTO_ITEM_GALLERY = 1
        const val RC_CAMERA_AND_READ_EXTERNAL: Int = 18
    }

    /**
     * 需要的危险权限
     */
    private val perms = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)

    private val mUploadManager: UploadManager by lazy { UploadManager() }

    private var mLocalFileUrl: String? = null
    private var mRemoteFileUrl: String? = null

    private var mUserIcon: String? = null
    private var mUserName: String? = null
    private var mUserMobile: String? = null
    private var mUserGender: String? = null
    private var mUserSign: String? = null

    private lateinit var mTakePhoto: TakePhoto
    private lateinit var mTempFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        mTakePhoto = TakePhotoImpl(this, this)
        mTakePhoto.onCreate(savedInstanceState)

        initView()
        initData()

    }

    /*
        初始化视图
     */
    private fun initView() {
        mUserIconView.onClick {
            showAlertView()
        }

        mHeaderBar.getRightView().onClick {
            mPresenter.editUser(this,mRemoteFileUrl!!,
                    mUserNameEt.text.toString() ?:"",
                    if(mGenderMaleRb.isChecked) "0" else "1",
                    mUserSignEt.text.toString() ?:""
                    )
        }
    }

    /*
        初始化数据
     */
    private fun initData() {
        mUserIcon = AppPrefsUtils.getString(ProviderConstant.KEY_SP_USER_ICON)
        mUserName = AppPrefsUtils.getString(ProviderConstant.KEY_SP_USER_NAME)
        mUserMobile = AppPrefsUtils.getString(ProviderConstant.KEY_SP_USER_MOBILE)
        mUserGender = AppPrefsUtils.getString(ProviderConstant.KEY_SP_USER_GENDER)
        mUserSign = AppPrefsUtils.getString(ProviderConstant.KEY_SP_USER_SIGN)

        mRemoteFileUrl = mUserIcon
        if (mUserIcon != ""){
            GlideUtils.loadUrlImage(this,mUserIcon!!,mUserIconIv)
        }
        mUserNameEt.setText(mUserName)
        mUserMobileTv.text = mUserMobile

        if (mUserGender == "0") {
            mGenderMaleRb.isChecked = true
        }
        else {
            mGenderFemaleRb.isChecked = true
        }

        mUserSignEt.setText(mUserSign)

    }

    /*
        Dagger注册
     */
    override fun injectComponent() {
        DaggerUserComponent.builder().userModule(UserModule()).build().inject(this)
        mPresenter.mView = this
    }

    private var itemPosition = 0


    private fun showAlertView() {
        AlertView("", "选择图片", "取消", null, arrayOf("拍照", "相册"), this,
                AlertView.Style.ActionSheet, OnItemClickListener { o, position ->

            this.itemPosition = position
            checkPermissions("相机权限和存储权限，是用来更换头像使用的。建议您开启", RC_CAMERA_AND_READ_EXTERNAL, perms, object : PermissionLisenter {
                override fun onPermissionsGranted() {
                    takePhoto()
                }

                override fun onPermissionsDenied() {

                }
            })

        }

        ).show()
    }

    /**
     * 获取头像图片
     */
    private fun takePhoto() {
        when (this.itemPosition) {
            TAKE_PHOTO_ITEM_CAPTURE -> takePickFromCapture()
            TAKE_PHOTO_ITEM_GALLERY -> takePickFromGallery()
        }
    }

    /**
     * 从相册获取图片
     */
    private fun takePickFromGallery() {
        //压缩图片
        mTakePhoto.onEnableCompress(CompressConfig.ofDefaultConfig(), false)
        mTakePhoto.onPickFromGallery()
    }

    /**
     * 拍照获取图片
     */
    private fun takePickFromCapture() {
        //压缩图片
        mTakePhoto.onEnableCompress(CompressConfig.ofDefaultConfig(), false)
        createTempFile()
        mTakePhoto.onPickFromCapture(Uri.fromFile(mTempFile))
    }

    /*
        新建临时文件
     */
    private fun createTempFile() {
        val tempFileName = "${DateUtils.curTime}.png"
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            this.mTempFile = File(Environment.getExternalStorageDirectory(), tempFileName)
            return
        }

        this.mTempFile = File(filesDir, tempFileName)
    }



    override fun takeSuccess(result: TResult?) {
        Log.d("takephoto", result?.image?.originalPath)
        Log.d("takephoto", result?.image?.compressPath)

        mLocalFileUrl = result?.image?.compressPath
        mPresenter.getUploadToken(this)

    }

    override fun takeCancel() {

    }

    override fun takeFail(result: TResult?, msg: String?) {
        Log.d("takephoto", msg)
    }

    override fun onGetUploadTokenResult(result: String) {
        mUploadManager.put(mLocalFileUrl, null, result, object : UpCompletionHandler {
            override fun complete(key: String?, info: ResponseInfo?, response: JSONObject?) {
                mRemoteFileUrl = BaseConstant.IMAGE_SERVER_ADDRESS + response?.get("hash")

                Log.d("test", mRemoteFileUrl)
                GlideUtils.loadUrlImage(this@UserInfoActivity, mRemoteFileUrl!!, mUserIconIv)
            }

        }, null)
    }


    var invokeParam: InvokeParam? = null

    override fun invoke(invokeParam: InvokeParam): PermissionManager.TPermissionType {
        val type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod())
        if (TPermissionType.WAIT == type) {
            this.invokeParam = invokeParam
        }
        return type
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        //take photo   以下代码为处理Android6.0、7.0动态权限所需
        val type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this)
        //easy permissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    /*
        TakePhoto默认实现
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //take photo
        mTakePhoto.onActivityResult(requestCode, resultCode, data)
    }


    /*
        编辑用户资料回调
     */
    override fun onEditUserResult(result: UserInfo) {
        toast("修改成功")
        UserPrefsUtils.putUserInfo(result)
    }
}
