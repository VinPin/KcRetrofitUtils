package com.vinpin.kcretrofitutils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * author : vinpin
 * e-mail : hearzwp@163.com
 * time   : 2020/9/14 14:39
 * desc   : 添加公共请求头拦截器
 */
class HeaderInterceptor(private val headers: Map<String, String>) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHeaders = original.headers
        val requestBuilder = original.newBuilder()
        // 添加统一通用header，不存在则添加，存在则不添加。
        for (key in headers.keys) {
            if (originalHeaders[key] == null) {
                headers[key]?.let {
                    requestBuilder.addHeader(key, it)
                }
            }
        }
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}

/**
 * author : vinpin
 * e-mail : hearzwp@163.com
 * time   : 2020/9/14 14:39
 * desc   : 缓存拦截器
 */
class CacheInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        //如果没有网络，则启用 FORCE_CACHE
        if (!isConnected(KcRetrofitUtils.getContext())) {
            request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .build()
        }
        val originalResponse = chain.proceed(request)
        return if (isConnected(KcRetrofitUtils.getContext())) {
            //有网的时候读接口上的@Headers里的配置
            val cacheControl = request.cacheControl.toString()
            originalResponse.newBuilder()
                .header("Cache-Control", cacheControl)
                .removeHeader("Pragma")
                .build()
        } else {
            originalResponse.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=3600")
                .removeHeader("Pragma")
                .build()
        }
    }
}

/**
 * 判断网络是否连接
 *
 * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
 *
 * @return `true`: 是<br></br>`false`: 否
 */
fun isConnected(context: Context): Boolean {
    val info: NetworkInfo? = (context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.activeNetworkInfo
    return info != null && info.isConnected
}