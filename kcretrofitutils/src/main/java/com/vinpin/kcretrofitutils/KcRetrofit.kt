package com.vinpin.kcretrofitutils

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * author : VinPin
 * e-mail : hearzwp@163.com
 * time   : 2020/9/14 14:39
 * desc   : Retrofit 封装类
 */
class KcRetrofit {

    private val mRetrofitBuilder: Retrofit.Builder by lazy {
        // com.google.gson.stream.MalformedJsonException
        val gson = GsonBuilder().setLenient().create()
        Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(baseUrl)
    }

    var baseUrl: String = ApiService.BASE_URL
        set(value) {
            field = value
            mRetrofitBuilder.baseUrl(value)
        }

    private var levelForBody: Boolean = true

    private var mOkHttpClient: OkHttpClient? = null

    fun client(client: OkHttpClient): KcRetrofit {
        mOkHttpClient = client
        return this
    }

    fun setLevel(body: Boolean): KcRetrofit {
        levelForBody = body
        return this
    }

    fun build(): Retrofit {
        if (mOkHttpClient == null) {
            mOkHttpClient = KcRetrofitUtils.defaultOkHttpClient(levelForBody)
        }
        return mRetrofitBuilder.client(mOkHttpClient!!).build()
    }
}