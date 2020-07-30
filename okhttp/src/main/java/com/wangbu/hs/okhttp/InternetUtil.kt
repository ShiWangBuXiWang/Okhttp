package com.wangbu.hs.okhttp

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import java.lang.Exception

/**
 * Created by hushen on 2020-7-30.
 * 2869254822@qq.com
 */
class InternetUtil {

    companion object {
        /**
         * 判断是否有网络连接
         */
        @SuppressLint("MissingPermission")
        fun isNetWorkConnected(context: Context): Boolean {
            val connectivityManager: ConnectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var networkinfo: NetworkInfo? = null
            if (connectivityManager != null) {
                networkinfo = connectivityManager.activeNetworkInfo
            }
            return networkinfo != null && networkinfo.isAvailable
        }

        /**
         * 判断WIFI网络是否可用
         */
        fun isWifiConnected(context: Context): Boolean {
            val connectivityManager: ConnectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var networkinfo: NetworkInfo? = null
            if (connectivityManager != null) {
                networkinfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            }
            return networkinfo != null && networkinfo.isAvailable
        }

        /**
         * 判断MOBILE网络是否可用
         */
        fun isMobileConnected(context: Context): Boolean {
            val connectivityManager: ConnectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var networkinfo: NetworkInfo? = null
            if (connectivityManager != null) {
                networkinfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            }
            return networkinfo != null && networkinfo.isAvailable
        }

        /**
         * 获取当前网络连接的类型信息
         */
        fun getConnectedType(context: Context): Int {
            var i: Int = -1
            val connectivityManager: ConnectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var networkinfo: NetworkInfo = connectivityManager.activeNetworkInfo
            if (networkinfo != null) {
                if (networkinfo.isAvailable) {
                    i = networkinfo.type
                }
            }
            return i
        }

        /**
         * 获取当前ip地址
         *
         * @param context
         * @return
         */
        fun getLocalIpAddress(context: Context): String {
            try {
                val wifiManager: WifiManager =
                    context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                var wifiInfo: WifiInfo = wifiManager.connectionInfo
                var i: Int = wifiInfo.ipAddress
                return int2ip(i)
            } catch (ex: Exception) {
                return " 获取IP出错!!!!请保证是WIFI,或者请重新打开网络!\n" + ex.message
            }
        }

        /**
         * 将ip的整数形式转换成ip形式
         *
         * @param ipInt
         * @return
         */
        /**
         *   java  >>   kotlin shr
         *   java &     kotlin and
         */
        fun int2ip(ipInt: Int): String {
            return (ipInt and 0xFF).toString() + "." + ((ipInt shr 8) and 0xFF).toString() + "." + ((ipInt shr 16) and 0xFF).toString() + "." + ((ipInt shr 24) and 0xFF).toString()
        }
    }


}