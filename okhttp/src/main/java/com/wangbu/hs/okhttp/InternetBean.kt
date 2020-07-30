package com.wangbu.hs.okhttp

/**
 * Created by hushen on 2020-7-30.
 * 2869254822@qq.com
 */
class InternetBean {
    var status: Boolean = false
    var msg: String? = null
    override fun toString(): String {
        return "InternetBean(status=$status, msg='$msg')"
    }
}