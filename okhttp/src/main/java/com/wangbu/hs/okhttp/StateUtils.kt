package com.wangbu.hs.okhttp

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * Created by hushen on 2020-7-30.
 * 2869254822@qq.com
 */
object StateUtils {
    @SuppressLint("MissingPermission")
    fun isNetworkAvailable(context: Context): Boolean {
        if (context != null) {
            var cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var info = cm.activeNetworkInfo
            if (info != null) {
                return info.isAvailable
            }
        }
        return false
    }
}