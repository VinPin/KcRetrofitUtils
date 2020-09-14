package com.vinpin.kcretrofitutils

import android.os.Environment
import android.text.TextUtils
import android.util.Log
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.internal.cache.CacheInterceptor
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.net.Proxy
import java.util.concurrent.TimeUnit

/**
 * author : VinPin
 * e-mail : hearzwp@163.com
 * time   : 2020/9/14 14:39
 * desc   : OkHttpClient 封装类
 */
class KcOkHttpClient {

    private val mOkHttpBuilder: OkHttpClient.Builder by lazy {
        OkHttpClient.Builder().proxy(Proxy.NO_PROXY)
    }

    fun addHeaders(headers: Map<String, String>): KcOkHttpClient {
        addInterceptor(HeaderInterceptor(headers))
        return this
    }

    fun readTimeout(second: Long): KcOkHttpClient {
        mOkHttpBuilder.readTimeout(second, TimeUnit.SECONDS)
        return this
    }

    fun writeTimeout(second: Long): KcOkHttpClient {
        mOkHttpBuilder.writeTimeout(second, TimeUnit.SECONDS)
        return this
    }

    fun connectTimeout(second: Long): KcOkHttpClient {
        mOkHttpBuilder.connectTimeout(second, TimeUnit.SECONDS)
        return this
    }

    fun addInterceptor(interceptor: Interceptor): KcOkHttpClient {
        mOkHttpBuilder.addInterceptor(interceptor)
        return this
    }

    fun debugLog(enable: Boolean, levelForBody: Boolean = true): KcOkHttpClient {
        if (enable) {
            val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    // 日志输出，发布时去掉。
                    Log.i("RcRetrofit", message)
                }
            })
            interceptor.level = if (levelForBody) HttpLoggingInterceptor.Level.BODY else
                HttpLoggingInterceptor.Level.HEADERS
            mOkHttpBuilder.addInterceptor(interceptor)
        }
        return this
    }

    fun sslSocketFactory(): KcOkHttpClient {
        mOkHttpBuilder.sslSocketFactory(SSLHelper.sslSocketFactory, SSLHelper.trustManager)
            .hostnameVerifier(SSLHelper.hostnameVerifier)
        return this
    }

    fun cachePath(cachePath: String?, maxSize: Long): KcOkHttpClient {
        val cacheInterceptor = CacheInterceptor()
        val cache = if (!TextUtils.isEmpty(cachePath) && maxSize > 0) {
            Cache(File(cachePath), maxSize)
        } else {
            Cache(
                File(Environment.getExternalStorageDirectory().path + "/rcRetrofitCacheData"),
                (1024 * 1024 * 100).toLong())
        }
        mOkHttpBuilder.addInterceptor(cacheInterceptor).addNetworkInterceptor(cacheInterceptor).cache(cache)
        return this
    }

    fun build(): OkHttpClient {
        return mOkHttpBuilder.build()
    }
}