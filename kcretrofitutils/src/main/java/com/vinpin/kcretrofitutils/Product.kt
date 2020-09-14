package com.vinpin.kcretrofitutils

/**
 * author : VinPin
 * e-mail : hearzwp@163.com
 * time   : 2020/9/14 14:39
 * desc   : 一个被区分的并集，其将成功结果封装为[T] 类型，或者将失败封装为任意[ApiErrorException]异常。
 */
class Product<out T> private constructor(val value: Any?) {
    /**
     * 如果此实例表示成功的结果，则返回“true”。
     */
    val isSuccess: Boolean get() = value !is Failure

    /**
     * 如果此实例表示失败的结果，则返回“true”。
     */
    val isFailure: Boolean get() = value is Failure

    /**
     * 如果此实例表示[success] [Product.isSuccess]，则返回封装的值；如果它是[failure] [Product.isFailure]，则返回null。
     */
    @Suppress("UNCHECKED_CAST")
    fun getOrNull(): T? = when {
        isFailure -> null
        else -> value as? T
    }

    /**
     * 如果此实例表示[failure] [isFailure]，则返回封装的异常；如果为[success] [isSuccess]，则返回null。
     */
    fun exceptionOrNull(): ApiErrorException? = when (value) {
        is Failure -> value.exception
        else -> null
    }

    override fun toString(): String = when (value) {
        is Failure -> value.toString()
        else -> "Success($value)"
    }

    companion object {
        /**
         * 返回一个实例，该实例将给定的[value]封装为成功值。
         */
        fun <T> success(value: T): Product<T> = Product(value)

        /**
         * 返回一个实例，该实例将给定的[exception]封装为失败。
         */
        fun <T> failure(exception: ApiErrorException): Product<T> = Product(Failure(exception))
    }

    internal class Failure(val exception: ApiErrorException) {

        override fun equals(other: Any?): Boolean = other is Failure && exception == other.exception

        override fun hashCode(): Int = exception.hashCode()

        override fun toString(): String = "Failure($exception)"
    }
}