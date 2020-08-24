package com.jibergroup.data.utils

import androidx.annotation.Keep
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.jibergroup.data.common.EmptyBodyException
import com.jibergroup.data.common.ParseDataException
import com.jibergroup.data.common.TimeOutException
import com.jibergroup.data.service.response.ListWrapper
import com.jibergroup.domain.utils.RequestResult
import kotlinx.coroutines.Deferred
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException

interface CoroutineCaller {
    suspend fun <T> coroutineApiCall(deferred: Deferred<Response<T>>): RequestResult<T>
    suspend fun <T> coroutineApiCallRaw(deferred: Deferred<T>): RequestResult<T>
    suspend fun <T> coroutineApiCallWrapper(deferred: Deferred<ListWrapper<T>>): RequestResult<T>
}

interface MultiCoroutineCaller : CoroutineCaller {
    suspend fun <T> multiCall(vararg requests: Deferred<Response<T>>): List<RequestResult<T>>

    suspend fun <T1, T2, R> zip(
        request1: Deferred<Response<T1>>,
        request2: Deferred<Response<T2>>,
        zipper: (RequestResult<T1>, RequestResult<T2>) -> R
    ): R

    suspend fun <T1, T2, T3, R> zip(
        request1: Deferred<Response<T1>>,
        request2: Deferred<Response<T2>>,
        request3: Deferred<Response<T3>>,
        zipper: (RequestResult<T1>, RequestResult<T2>, RequestResult<T3>) -> R
    ): R

    suspend fun <T1, T2, T3, T4, R> zip(
        request1: Deferred<Response<T1>>,
        request2: Deferred<Response<T2>>,
        request3: Deferred<Response<T3>>,
        request4: Deferred<Response<T4>>,
        zipper: (RequestResult<T1>, RequestResult<T2>, RequestResult<T3>, RequestResult<T4>) -> R
    ): R

    suspend fun <T, R> zipArray(
        vararg requests: Deferred<Response<T>>,
        zipper: (List<RequestResult<T>>) -> R
    ): R
}

class ApiCaller : MultiCoroutineCaller {

    companion object {

        //        val serverError = LocaleHolder.string("mobile_message_internal_server_error", "Внутренняя ошибка сервера")
        val serverError = "Внутренняя ошибка сервера"

        /**
         * Константа только для ОТП fragment-a [OTPFragment]
         * означает то-что ОТП фрагмент не закрывается
         * при любой другой 500-ой ошибке нужно закрывать ОТП фрагмент
         */
        val HTTP_CODE_CLOSE_OTP = 2

        /**
         * [SMS_VALID_TIME_EXPIRED] и [WRONG_OTP]
         * действуют только на отп fragment-е
         * приходят от сервера при 500-ой ошибке в поле message
         * см [HTTP_CODE_CLOSE_OTP] этот код ошибки
         */
        val SMS_VALID_TIME_EXPIRED = "error.sms_valid_time_expired"
        val WRONG_OTP = "sms.wrong_otp"
    }

    val HTTP_CODE_TOKEN_EXPIRED = 420
    val HTTP_CODE_ACCOUNT_BLOCKED = 419

    /**
     * Обработчик запросов на `kotlin coroutines`
     * ждет выполнения запроса [deferred]
     * обрабатывает ошибки сервера
     * обрабатывает ошибки соединения
     * возвращает [RequestResult.Success] или [RequestResult.Error]
     */
    override suspend fun <T> coroutineApiCall(deferred: Deferred<Response<T>>): RequestResult<T> =
        try {
            handleResult(deferred.await())
        } catch (e: Exception) {
            handleException(e)
        }

    override suspend fun <T> coroutineApiCallRaw(deferred: Deferred<T>): RequestResult<T> = try {
        RequestResult.Success(deferred.await())
    } catch (e: Exception) {
        handleException(e)
    }

    override suspend fun <T> coroutineApiCallWrapper(deferred: Deferred<ListWrapper<T>>): RequestResult<T> {
        return try {
            val await = deferred.await()
            RequestResult.Success<T>(await.items)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    /**
     * Обработчик для нескольких запросов на `kotlin coroutines`
     * запускает все [requests] и записывает их в массив [RequestResult]
     * обрабатывает ошибки сервера при помощи [coroutineApiCall]
     * обрабатывает ошибки соединения при помощи [coroutineApiCall]
     *   пока есть ограничение: можно делать только однородные запросы
     *   то есть [requests] должны возвращать либо один тип данных, либо общий интерфейс
     */
    override suspend fun <T> multiCall(vararg requests: Deferred<Response<T>>): List<RequestResult<T>> =
        requests.map {
            coroutineApiCall(it)
        }

    /**
     * Обработчик для однородных запросов на `kotlin coroutines`
     * [requests] должны возвращать один тип данных
     * запускает все [requests], записывает их в массив [RequestResult]
     * и передает в обработчик [zipper]
     * обрабатывает ошибки сервера при помощи [coroutineApiCall]
     * обрабатывает ошибки соединения при помощи [coroutineApiCall]
     */
    override suspend fun <T, R> zipArray(
        vararg requests: Deferred<Response<T>>,
        zipper: (List<RequestResult<T>>) -> R
    ): R = zipper(requests.map { coroutineApiCall(it) })

    /**
     * Обработчик для двух разнородных запросов на `kotlin coroutines`
     * запускает [request1], [request2] и передает в обработчик [zipper]
     * обрабатывает ошибки сервера при помощи [coroutineApiCall]
     * обрабатывает ошибки соединения при помощи [coroutineApiCall]
     */
    override suspend fun <T1, T2, R> zip(
        request1: Deferred<Response<T1>>,
        request2: Deferred<Response<T2>>,
        zipper: (RequestResult<T1>, RequestResult<T2>) -> R
    ): R = zipper(coroutineApiCall(request1), coroutineApiCall(request2))

    /**
     * Обработчик для трех разнородных запросов на `kotlin coroutines`
     * запускает [request1], [request2], [request3] и передает в обработчик [zipper]
     * обрабатывает ошибки сервера при помощи [coroutineApiCall]
     * обрабатывает ошибки соединения при помощи [coroutineApiCall]
     */
    override suspend fun <T1, T2, T3, R> zip(
        request1: Deferred<Response<T1>>,
        request2: Deferred<Response<T2>>,
        request3: Deferred<Response<T3>>,
        zipper: (RequestResult<T1>, RequestResult<T2>, RequestResult<T3>) -> R
    ): R =
        zipper(coroutineApiCall(request1), coroutineApiCall(request2), coroutineApiCall(request3))

    /**
     * Обработчик для трех разнородных запросов на `kotlin coroutines`
     * запускает [request1], [request2], [request3] и передает в обработчик [zipper]
     * обрабатывает ошибки сервера при помощи [coroutineApiCall]
     * обрабатывает ошибки соединения при помощи [coroutineApiCall]
     */
    override suspend fun <T1, T2, T3, T4, R> zip(
        request1: Deferred<Response<T1>>,
        request2: Deferred<Response<T2>>,
        request3: Deferred<Response<T3>>,
        request4: Deferred<Response<T4>>,
        zipper: (RequestResult<T1>, RequestResult<T2>, RequestResult<T3>, RequestResult<T4>) -> R
    ): R = zipper(
        coroutineApiCall(request1),
        coroutineApiCall(request2),
        coroutineApiCall(request3),
        coroutineApiCall(request4)
    )

    private fun <T> handleResult(result: Response<T>): RequestResult<T> {
        return if (result.isSuccessful) {
            RequestResult.Success(result.body())
        } else {
            throw HttpException(result)
        }
    }

    private fun <T> handleException(e: Exception): RequestResult<T> {
        return when (e) {
            is com.jibergroup.data.common.HttpException -> {
                RequestResult.Error(e.message(), 0)
            }
            is EmptyBodyException -> {
                RequestResult.Error(null, 0).also { it.errorMessageId = e.getDefaultResourceId() }
            }
            is TimeOutException -> {
                RequestResult.Error(null, 0).also { it.errorMessageId = e.getDefaultResourceId() }
            }
            is ParseDataException -> {
                RequestResult.Error(null, 0).also { it.errorMessageId = e.getDefaultResourceId() }
            }
            is SocketTimeoutException -> {
                RequestResult.Error(e.message, 0)
            }
            else -> {
                RequestResult.Error(e.message, 0)
            }
        }
    }

}

/**
 * Презентация ответов сервера для `Presentation layer`
 * должно возвращаться репозиториями, использующими [CoroutineCaller], [RxSingleCaller] или [CoroutineRxCaller]
 * //todo implement [ResourceString] для [Error]
 */

/**
 * Модель ошибок сервера
 * //todo оставить только нужные поля
 */
@Keep
data class ErrorResponse(
    val timestamp: String?,
    val status: Int?,
    val error: String?,
    val exception: String?,
    val message: String?,
    val description: String?,
    val path: String?,
    val authToken: String?,
    val fio: String?,
    val lastLogin: String?,
    val companies: String?,
    val code: String?,
    val value: String?,
    val privileges: String?,
    val translationKey: String?,
    val error_description: String?
) {

    fun print(default: String): String {
        return error_description ?: description ?: value ?: translationKey ?: message ?: default
    }

    companion object {
        /**
         * Парсинг ответа сервера вручную в объект [ErrorResponse]
         *
         * Этот метод должен оставаться приватным
         * (ничего страшного, что каждый раз создаётся новый объект)
         */
        @Throws(JsonSyntaxException::class)
        private fun from(response: String): ErrorResponse {
            return Gson().fromJson(response, ErrorResponse::class.java)
        }

        fun print(response: String, default: String) = try {
            from(response).print(default)
        } catch (e: Exception) {
            default
        }

        /**
         * Проверка условия для ответа сервера
         *
         * например, мы хотим выяснить, есть ли в ответе поле [error_description]
         * и равно ли оно "User locked"
         */
        fun checkCondition(response: String, condition: ErrorResponse.() -> Boolean) = try {
            from(response).condition()
        } catch (e: Exception) {
            false
        }
    }
}