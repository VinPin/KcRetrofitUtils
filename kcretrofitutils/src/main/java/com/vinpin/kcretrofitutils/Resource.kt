package com.vinpin.kcretrofitutils

/**
 * author : VinPin
 * e-mail : hearzwp@163.com
 * time   : 2020/9/14 14:39
 * desc   : 封装数据及其状态的 Resource 类来公开网络状态
 */
class Resource<out T> private constructor(val status: Status, val product: Product<T>?) {

    val isLoading: Boolean get() = status == Status.LOADING

    val isSuccess: Boolean get() = status == Status.SUCCESS

    val isFailure: Boolean get() = status == Status.FAILURE

    fun loadingOrNull(): Boolean? = when {
        isLoading -> true
        else -> null
    }

    fun getOrNull(): T? = when {
        isLoading -> null
        else -> product?.getOrNull()
    }

    fun exceptionOrNull(): ApiErrorException? = when {
        isLoading -> null
        else -> product?.exceptionOrNull()
    }

    companion object {

        fun <T : Any> loading() = Resource<T>(Status.LOADING, null)

        fun <T : Any> success(data: T) = Resource(Status.SUCCESS, Product.success(data))

        fun <T : Any> failure(exception: ApiErrorException) = Resource<T>(Status.FAILURE, Product.failure(exception))
    }
}

/**
 * 一种表示数据网络状态的枚举类
 */
enum class Status {
    // 加载
    LOADING,

    // 成功
    SUCCESS,

    // 失败
    FAILURE
}