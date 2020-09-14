package com.vinpin.kcretrofitutils

import android.content.Context
import okhttp3.OkHttpClient

/**
 * author : VinPin
 * e-mail : hearzwp@163.com
 * time   : 2020/9/14 14:39
 * desc   : 获取KcRetrofit实例的工具类
 */
object KcRetrofitUtils {

    private lateinit var mContext: Context
    private var defaultKcRetrofit: KcRetrofit? = null

    /**
     * 初始化，放在Application中的onCreate()中执行。
     */
    fun init(context: Context) {
        mContext = context
    }

    fun getContext(): Context = mContext

    /**
     * 获取默认的KcRetrofit实例，该实例只会被创建一次。可以作为全局使用，一般不再修改其属性。
     */
    fun getDefault(): KcRetrofit {
        if (defaultKcRetrofit == null) {
            defaultKcRetrofit = newInstance()
        }
        return defaultKcRetrofit!!
    }

    /**
     * 创建新的KcRetrofit实例
     */
    fun newInstance(): KcRetrofit {
        return KcRetrofit()
    }

    /**
     * 创建默认的OkHttpClient实例
     */
    fun defaultOkHttpClient(levelForBody: Boolean = true): OkHttpClient {
        return KcOkHttpClient()
            .sslSocketFactory()
            .debugLog(BuildConfig.DEBUG, levelForBody)
            .build()
    }
}