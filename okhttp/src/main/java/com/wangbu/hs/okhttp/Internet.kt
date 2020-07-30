package com.wangbu.hs.okhttp

import android.content.Context

/**
 * Created by hushen on 2020-7-30.
 * 2869254822@qq.com
 */
class Internet {
    companion object {
        /**
         * 判断网络
         */
        fun ifInternet(context: Context): InternetBean {
            var msg: InternetBean = InternetBean()
            when (InternetUtil.getConnectedType(context)) {
                -1 -> {
                    msg.status = false
                    msg.msg = "网络异常"
                    return msg
                }
                -0 -> if (!InternetUtil.isNetWorkConnected(context)) {
                    msg.status = false
                    msg.msg = "网络异常"
                    return msg
                } else {
                    msg.status = true
                    return msg
                }

                1 -> if (!InternetUtil.isNetWorkConnected(context)) {
                    msg.status = false
                    msg.msg = "WIFI网络异常"
                    return msg
                } else {
                    msg.status = true
                    return msg
                }

                else -> if (!InternetUtil.isNetWorkConnected(context)) {
                    msg.status = false
                    msg.msg = "网络异常"
                    return msg
                } else {
                    msg.status = true
                    return msg
                }

            }
        }
    }
}