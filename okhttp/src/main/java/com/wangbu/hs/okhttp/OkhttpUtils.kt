package com.wangbu.hs.okhttp

import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.annotation.NonNull
import com.taihao.jinxiaocukotlin.internetutil.*
import com.taihao.jinxiaocukotlin.utils.StateUtils
import com.taihao.jinxiaocukotlin.utils.StringUtil
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Created by hushen on 2020-7-30.
 * 2869254822@qq.com
 */
class OkhttpUtils(private val mContext: Context) {
    private var mCacheUrl: String = ""
    private var methodCode: String = ""
    private var requestType: RequestType? = null

    private fun request(url: String, omap: Map<Any, Any>, onOkHttpListener: onOkHttpListener) {
        getObservable(url, omap).subscribeOn(Schedulers.io()).observeOn(AndroidScheduler.mainThread())
            .subscribe(object : Observer<String> {
                override fun onComplete() {
                    onOkHttpListener.onCompleted()
                }

                override fun onSubscribe(d: Disposable?) {
                }

                override fun onNext(t: String?) {
                    onOkHttpListener.onSuccess(t!!)
                }

                override fun onError(e: Throwable?) {
                    onOkHttpListener.onFailure(e!!)
                }
            }
            )
    }

    fun getObservable(url: String, omap: Map<Any, Any>): Observable<String> {
        return Observable.unsafeCreate { observer: Observer<in String>? -> send(url, omap, observer) }
    }

    fun send(url: String, oman: Map<Any, Any>, observer: Observer<in String>?) {
        if (oman != null && oman.size > 0) {
            mCacheUrl = url + oman.toString()
        } else {
            mCacheUrl = url
        }
        var internetBean: InternetBean = Internet.ifInternet(mContext)
        if (internetBean.status) {
            val call: Call = okHttpClient!!.newCall(getRequest(url, oman))
            sendCall(call, observer!!)
        } else {
            var json = CacheUtils.getInstance(mContext).getCacheToLocalJson(mCacheUrl)
            if (!TextUtils.isEmpty(json)) {
                observer!!.onNext(json)
            } else {
                observer!!.onError(Error(internetBean.msg))
            }
            observer.onComplete()
        }
    }

    fun getRequest(url: String, oman: Map<Any, Any>): Request {
        when (requestType) {
            RequestType.POST -> return postRequest(url, oman)
            RequestType.SOAP -> return soapRequest(url, oman)
            else -> return postRequest(url, oman)
        }
    }

    private fun postRequest(url: String, oman: Map<Any, Any>): Request {
        val builder: FormBody.Builder = FormBody.Builder()
        if (oman != null && oman.isNotEmpty()) {
            for ((key, value) in oman) {
                builder.add(key.toString(), value.toString())
            }
        }
        val build = builder.build()
        return Request.Builder()
            .url(url)
            .post(build)
            .build()
    }

    private fun soapRequest(url: String, oman: Map<Any, Any>): Request {
        val sb: StringBuilder = StringBuilder()
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
        sb.append("<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">")
        sb.append("<soap12:Body>")
        sb.append("<_PharmacyAffairApp xmlns=\"http://taihaoTech.org/\">")
        sb.append("<strXML>")
        val ssb: StringBuilder = StringBuilder()
        ssb.append("<?xml version=\"1.0\" encoding=\"GB2312\"?><ROOT><MethodCode>")
        ssb.append(methodCode)
        ssb.append("</MethodCode>")
        if (StringUtil.isMapNotEmpty(oman)) {
            for ((key, value) in oman) {
                ssb.append("<").append(key.toString()).append(">").append(value.toString()).append("</")
                    .append(key.toString()).append(">")
            }
        }
        ssb.append("</ROOT>")
        Log.d("请求", ssb.toString())
        sb.append(Base64Object.stringToBase64(ssb.toString()))
        sb.append("</strXML>")
        sb.append("</_PharmacyAffairApp>")
        sb.append("</soap12:Body>")
        sb.append("</soap12:Envelope>")
        Log.d("请求", sb.toString())
        return Request.Builder()
            .url(url)
            .post(RequestBody.create(mediaType, sb.toString()))
            .build()
    }


    fun post(url: String, map: Map<Any, Any>, onOkHttpListener: onOkHttpListener) {
        this.requestType = RequestType.POST
        request(url, map, onOkHttpListener)
    }

    fun soap(url: String, methodCode: String, oman: Map<Any, Any>, onOkHttpListener: onOkHttpListener) {
        this.methodCode = methodCode
        this.requestType = RequestType.SOAP
        request(url, oman, onOkHttpListener)
    }

    fun sendCall(call: Call, observer: Observer<in String>) {
        call.enqueue(object : Callback {
            override fun onFailure(@NonNull call: Call, @NonNull e: IOException) {
                var js = CacheUtils.getInstance(mContext).getCacheToLocalJson(mCacheUrl)
                if (!TextUtils.isEmpty(js)) {
                    observer.onNext(js)
                } else {
                    observer.onError(e)
                }
                observer.onComplete()
                e.printStackTrace()
            }

            @Throws(IOException::class)
            override fun onResponse(@NonNull call: Call, @NonNull response: Response) {
                var str = response.body!!.string()
                if (!str.toUpperCase().contains(("<!doctype html>").toUpperCase())) {
                    if (!TextUtils.isEmpty(mCacheUrl)) {
                        CacheUtils.getInstance(mContext).setCacheToLocalJson(mCacheUrl, str)
                    }
                    observer.onNext(str)
                } else if (StringUtil.isNotEmpty(mCacheUrl)) {
                    observer.onNext(CacheUtils.getInstance(mContext).getCacheToLocalJson(mCacheUrl))
                } else {
                    observer.onNext(str)
                }
                observer.onComplete()
            }
        })
    }


    companion object { //伴生对象

        private var instance: OkhttpUtils? = null
        private val mediaType: MediaType? = "application/soap+xml; charset=utf-8".toMediaTypeOrNull()
        private var okHttpClient: OkHttpClient? = null
        fun getInstance(context: Context):OkhttpUtils?{
            if(instance==null){
                synchronized(OkhttpUtils::class.java){
                    if(instance ==null){
                        instance = OkhttpUtils(context)
                        var cacheSize: Int = 10 * 1024 * 1024//10mb
                        var httpCacheDirectory: File = File(context!!.cacheDir, "responses")
                        var cache: Cache = Cache(httpCacheDirectory, cacheSize.toLong())
                        var REWRITE_CACHE_CONTROL_INTERCEPTOR: Interceptor = Interceptor { chain ->
                            val cacheBuilder: CacheControl.Builder = CacheControl.Builder()
                            cacheBuilder.maxAge(0, TimeUnit.SECONDS)
                            cacheBuilder.maxStale(365, TimeUnit.DAYS)
                            val cacheControl = cacheBuilder.build()
                            var request: Request = chain.request()
                            if (!StateUtils.isNetworkAvailable(context!!)) {
                                request = request.newBuilder().cacheControl(cacheControl).build()
                            }
                            val originalResponse: Response = chain.proceed(request)
                            if (StateUtils.isNetworkAvailable(context!!)) {
                                var maxAge: Int = 0
                                originalResponse.newBuilder()
                                    .removeHeader("Pragma")
                                    .header("Cache-Control", "public ,max-age=$maxAge")
                                    .build()
                            } else {
                                var maxStale: Int = 60 * 60 * 24 * 28
                                originalResponse.newBuilder()
                                    .removeHeader("Pragma")
                                    .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                                    .build()
                            }
                        }
                        okHttpClient = OkHttpClient().newBuilder()
                            .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR).cache(cache)
                            .build()
                    }
                }
            }
            return instance
        }

    }

}