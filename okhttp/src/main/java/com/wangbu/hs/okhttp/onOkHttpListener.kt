package com.wangbu.hs.okhttp

/**
 * Created by hushen on 2020-7-30.
 * 2869254822@qq.com
 */
interface onOkHttpListener {
    fun onCompleted()
    fun onSuccess(s: String)
    fun onFailure(t: Throwable)
}