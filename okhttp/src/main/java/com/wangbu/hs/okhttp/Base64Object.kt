package com.wangbu.hs.okhttp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

import java.io.ByteArrayOutputStream
import java.io.IOException

/**
 * Created by hushen on 2020-7-30.
 * 2869254822@qq.com
 */

object Base64Object {
    /**
     * 把base64的String码转换成正常显示的字符串
     */
    fun base64ToString(base64: String): String {
        val decode = Base64Util.decode(base64)
        return String(decode)
    }

    /**
     * 把String的转换成base64码
     */
    fun stringToBase64(ss: String): String? {
        val bytes = ss.toByteArray()
        return Base64Util.encode(bytes)
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    fun bitmapToBase641(bitmap: Bitmap?): String? {
        var result: String? = null
        var baos: ByteArrayOutputStream? = null
        try {
            if (bitmap != null) {
                baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                baos.flush()
                baos.close()
                val bitmapBytes = baos.toByteArray()
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (baos != null) {
                    baos.flush()
                    baos.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return result
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    fun base64ToBitmap(base64Data: String): Bitmap {
        val bytes = Base64.decode(base64Data, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

}
