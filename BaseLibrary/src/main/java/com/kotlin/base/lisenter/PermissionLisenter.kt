package com.kotlin.base.lisenter

/**
 * Description:
 * Author: devil
 * Date: 2018/9/7
 */
interface PermissionLisenter {
    fun onPermissionsGranted()
    fun onPermissionsDenied()
}