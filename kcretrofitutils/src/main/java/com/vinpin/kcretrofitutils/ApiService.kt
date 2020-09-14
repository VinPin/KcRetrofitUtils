package com.vinpin.kcretrofitutils

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

/**
 * author : vinpin
 * e-mail : hearzwp@163.com
 * time   : 2020/9/14 14:39
 * desc   : HTTP API接口
 */
interface ApiService {

    companion object {
        const val BASE_URL = "https://vinpin.com/"
    }

    @GET
    fun get(
        @Url url: String,
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap queryMap: Map<String, String>
    ): Call<String>

    @POST
    fun post(
        @Url url: String,
        @HeaderMap headerMap: Map<String, String>,
        @Body requestBody: RequestBody
    ): Call<String>
}