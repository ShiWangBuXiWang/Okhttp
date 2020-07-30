package com.wangbu.hs.okhttp

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * 字符串工具类
 */
object StringUtil {


    val EMPTY = ""

    val dateformat = SimpleDateFormat("HH:mm", Locale.CHINA)

    val currentTime: String
        get() = dateformat.format(Date())

    val sdformat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)

    val currentDateTime: String
        get() = sdformat.format(Date())

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return true 不为空, false 为空
     */
    fun isNotEmpty(str: String?): Boolean {
        return str != null && "null" != str && str.trim { it <= ' ' }.length != 0
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return true 为空，false 不为空
     */
    fun isEmpty(str: String?): Boolean {
        return str == null || "null" == str || str.trim { it <= ' ' }.length == 0
    }

    fun formatDateTime(millseconds: Long): String {
        return sdformat.format(Date(millseconds))
    }

    /**
     * 判断集合是否为空
     */
    fun <T> isCollectionsNotEmpty(collection: Collection<T>?): Boolean {
        return collection != null && collection.size > 0
    }

    /**
     * 判断MAP是否为空
     */
    fun <K, V> isMapNotEmpty(map: Map<K, V>?): Boolean {
        return map != null && map.size > 0
    }

    /**
     * 判断List是否为空
     */
    fun isListEmpty(array: List<*>?): Boolean {
        return array != null && array.size == 0
    }

    /**
     * 判断JSON数组是否为空
     */
    fun isJSONArrayEmpty(array: JSONArray?): Boolean {
        return array == null || array.length() == 0
    }

    fun isObjectNotNull(`object`: Any?): Boolean {
        if (`object` != null && `object`.javaClass.isArray) {
            // 如果是数组类型
            throw UnsupportedOperationException("isObjectNotNull not supported operation :$`object`")
        }
        return `object` != null
    }

    /**
     * 判断JSON数据不空为
     */
    fun isJSONArrayNotEmpty(array: JSONArray?): Boolean {
        return array != null && array.length() > 0
    }

    /**
     * 判断JSON数组是否为空
     */
    fun isJSONObjectEmpty(`object`: JSONObject?): Boolean {
        return `object` == null || `object`.length() == 0
    }

    /**
     * 判断JSON数据不空为
     */
    fun isJSONObjectNotEmpty(`object`: JSONObject?): Boolean {
        return `object` != null && `object`.length() > 0
    }

    fun isIntArrayNotEmpty(array: IntArray?): Boolean {
        return array != null && array.size > 0
    }

    /**
     * 判断List数据不空为
     */
    fun isListNotEmpty(array: List<*>?): Boolean {
        return array != null && array.size > 0
    }

    fun isStringArrayNoEmpty(array: Array<String>?): Boolean {
        return array != null && array.size > 0
    }

    /**
     * 判断long数组不为空
     *
     * @param array
     * @return
     */
    fun isLongArrayNotEmpty(array: LongArray?): Boolean {
        return array != null && array.size > 0
    }

    /**
     * 判断float数组不为空
     *
     * @param array
     * @return
     */
    fun isFloatArrayNotEmpty(array: FloatArray?): Boolean {
        return array != null && array.size > 0
    }

    /**
     * 判断double数组不为空
     *
     * @param array
     * @return
     */
    fun isDoubleArrayNotEmpty(array: DoubleArray?): Boolean {
        return array != null && array.size > 0
    }

    /**
     * 该方法主要使用正则表达式来判断字符串中是否包含字母
     *
     * @param cardNum
     * @return 返回是否包含
     */
    fun isJudge(cardNum: String): Boolean {
        val regex = ".*[a-zA-Z]+.*"
        val m = Pattern.compile(regex).matcher(cardNum)
        return m.matches()
    }

    fun isNotBlank(str: String?): Boolean {
        return str != null && str.length != 0
    }

    fun isBlank(str: String?): Boolean {
        return str == null || str.length == 0
    }

    fun isNotTrimBlank(str: String?): Boolean {
        return str != null && str.trim { it <= ' ' }.length != 0
    }

    fun isTrimBlank(str: String?): Boolean {
        return str == null || str.trim { it <= ' ' }.length == 0
    }

    /**
     * 判断是否是身份证
     *
     * @param idNo
     * @return
     */
    fun isIdNo(idNo: String): Boolean {
        if (isTrimBlank(idNo)) {
            return false
        }
        val p =
            Pattern.compile("^([1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3})|([1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[X,x]))$")
        val matcher = p.matcher(idNo)
        return !matcher.find()
    }

    /**
     * 判断是否为手机号
     *
     * @param mobiles
     * @return
     */
    fun isNotMobileNO(mobiles: String): Boolean {
        val p = Pattern.compile("^((1[358][0-9])|(14[57])|(17[0678]))\\d{8}$")
        val m = p.matcher(mobiles)
        return !m.matches()
    }

    /**
     * 判断是否为邮箱号
     *
     * @param email
     * @return
     */
    fun isEmail(email: String): Boolean {
        if (isTrimBlank(email)) {
            return false
        }
        val str =
            "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$"
        val p = Pattern.compile(str)
        val m = p.matcher(email)
        return m.matches()
    }

    /**
     * 在HTML特殊字符的处理
     *
     * @param source
     * @return
     */
    fun htmlEscapeCharsToString(source: String): String? {
        return if (StringUtil.isEmpty(source))
            source
        else
            source.replace("&lt;".toRegex(), "<")
                .replace("&gt;".toRegex(), ">")
                .replace("&amp;".toRegex(), "&")
                .replace("&quot;".toRegex(), "\"")
                .replace("&copy;".toRegex(), "©")
                .replace("&yen;".toRegex(), "¥")
                .replace("&divide;".toRegex(), "÷")
                .replace("&times;".toRegex(), "×")
                .replace("&reg;".toRegex(), "®")
                .replace("&sect;".toRegex(), "§")
                .replace("&pound;".toRegex(), "£")
                .replace("&cent;".toRegex(), "￠")
    }

    /**
     * 验证用户名是否合法
     *
     * @param id
     * @return
     */
    fun isNotUserName(id: String): Boolean {
        if (isTrimBlank(id)) {
            return false
        }
        // 字母开头，由字母，数字和下划线组成的长度为2到16的字符串
        val p = Pattern.compile("^[a-zA-Z0-9_-]{2,16}$")
        val m = p.matcher(id)
        return !m.find()
    }

    fun isNotPassWord(password: String): Boolean {
        if (isTrimBlank(password)) {
            return false
        }
        // 就是以大小写字母开头，由大小写字母，数字和下划线组成的长度为6到18的字符串
        val p = Pattern.compile("^[a-zA-Z0-9_]{6,18}$")
        val m = p.matcher(password)
        return !m.find()
    }

    /**
     * 判断银行卡号是否合法
     *
     * @param bankCard
     * @return
     */
    fun isNotBank(bankCard: String): Boolean {
        if (isTrimBlank(bankCard)) {
            return false
        }
        // 一共16或19位，都是数字。
        val p = Pattern.compile("^\\d{16}$|^\\d{19}$")
        val m = p.matcher(bankCard)
        return !m.find()
    }

    /**
     * @param context
     * @param resId
     * @param str
     * @return
     */
    fun isStringFormat(context: Context, resId: Int, str: String): String {
        return String.format(context.resources.getString(resId), str)
    }

    /**
     * 从Raw文件中读取
     *
     * @param context
     * @param resId
     * @return
     */
    fun getFromRaw(context: Context, resId: Int): String? {
        try {
            val inputReader = InputStreamReader(context.resources.openRawResource(resId))
            val bufReader = BufferedReader(inputReader)
            val Result = StringBuilder()
            while (bufReader.readLine() != null)
                Result.append(bufReader.readLine())
            return Result.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 直接从assets读取
     */
    fun getFromAssets(context: Context, fileName: String): String? {
        try {
            val inputReader = InputStreamReader(context.resources.assets.open(fileName))
            val bufReader = BufferedReader(inputReader)
            val Result = StringBuilder()
            while (bufReader.readLine() != null)
                Result.append(bufReader.readLine())
            return Result.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param str
     * @return
     */
    fun subZeroAndDot(str: Any): String {
        var s = str.toString()
        if (isEmpty(s))
            return ""
        if (s.indexOf(".") > 0) {
            s = s.replace("0+?$".toRegex(), "")//去掉多余的0
            s = s.replace("[.]$".toRegex(), "")//如最后一位是.则去掉
        }
        return s
    }

    fun StringtoInt(string: String): Int {

        return Integer.parseInt(string)
    }
}
