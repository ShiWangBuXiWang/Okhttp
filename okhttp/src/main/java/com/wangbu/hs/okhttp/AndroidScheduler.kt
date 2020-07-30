package com.wangbu.hs.okhttp

import android.os.Handler
import android.os.Looper
import androidx.annotation.NonNull
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.Executor

/**
 * Created by hushen on 2020-7-30.
 * 2869254822@qq.com
 */
class AndroidScheduler:Executor{
    private val mMainScheduler: Scheduler
    private val mHandler: Handler
    override fun execute(@NonNull command: Runnable?) {
        mHandler.post(command)
    }

    companion object {
        private var instance: AndroidScheduler? = null

        @Synchronized
        fun mainThread(): Scheduler {
            if (instance == null) {
                instance = AndroidScheduler()
            }
            return instance!!.mMainScheduler
        }
    }

    init {
        mHandler = Handler(Looper.myLooper())
        mMainScheduler = Schedulers.from(this)
    }
}