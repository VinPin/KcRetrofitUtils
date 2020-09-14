package com.vinpin.kcretrofitutils

import org.json.JSONObject

/**
 * author : VinPin
 * e-mail : hearzwp@163.com
 * time   : 2020/9/14 14:39
 * desc   : 封装的异常类
 */
class ApiErrorException(var errorCode: Int, var errorMsg: String) : Exception(errorMsg) {

    /**
     * 原始的异常对象
     */
    var error: Throwable? = null

    companion object {

        const val TAG = "ApiErrorException"

        /** 根据指定的错误json字符串来创建对应的异常 */
        fun createApiErrorException(
            jsonString: String?,
            default: ApiErrorException
        ): ApiErrorException {
            var apiErrorException: ApiErrorException? = null
            jsonString?.let {
                try {
                    val jsonObject = JSONObject(jsonString)
                    val errcode = jsonObject.optInt("code")
                    val errmsg = jsonObject.optString("message")
                    apiErrorException = ApiErrorException(errcode, errmsg)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return apiErrorException ?: default
        }
    }
}