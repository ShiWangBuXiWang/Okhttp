package com.wangbu.hs.okhttp

import android.content.Context
import android.os.Environment
import java.io.*

/**
 * Created by hushen on 2020-7-30.
 * 2869254822@qq.com
 */
class CacheUtils {
    companion object {
        private var realFile: File? = null
        private var context: Context? = null
        private var instance: CacheUtils? = null

        fun getInstance(context: Context): CacheUtils {
            if (instance == null) {
                instance = CacheUtils()
            }
            var dir: File = context.getExternalFilesDir("null")!!
            if (dir != null && !dir.exists() && Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                dir.mkdir()
            }
            realFile = dir
            return instance!!
        }
    }

    /**
     * 根据url的MD5作为文件名,进行缓存
     *
     * @param url  文件名
     * @param json
     */
    fun setCacheToLocalJson(url: String, json: String) {
        val urlMD5 = MD5keyUtil.newInstance().getkeyBeanofStr(url)
        val path = realFile!!.getAbsolutePath() + "/" + urlMD5
        try {
            val file = File(path)
            if (file.exists()) {
                file.delete()
            }
            val fis = FileOutputStream(file)
            val bw = BufferedWriter(OutputStreamWriter(fis))
            val currentTime = System.currentTimeMillis()
            bw.write(currentTime.toString() + "")
            bw.newLine()
            bw.write(json)
            bw.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 根据缓存地址，从缓存中取出数据
     *
     * @param url
     * @return
     */
    fun getCacheToLocalJson(url: String): String? {
        val sb = StringBuffer()
        val urlMD5 = MD5keyUtil.newInstance().getkeyBeanofStr(url)
        // 创建缓存文件夹
        val file = File(realFile, urlMD5)
        if (file.exists()) {
            try {
                val fr = FileReader(file)
                val br = BufferedReader(fr)
                br.readLine()
                var temp: String = br.readLine()
                while (temp != null) {
                    sb.append(temp)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }

        }
        return sb.toString()
    }
}