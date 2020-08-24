package com.jibergroup.data.common

import com.jibergroup.data.R
import retrofit2.Response


class HttpException(private val response: Response<*>) : RuntimeException(
    getMessage(
        response
    )
) {

    private val code: Int = response.code()
    private val statusMessage: String = response.message()

    companion object {
        private fun getMessage(response: Response<*>): String {
            checkNotNull(
                response,
                "response == null"
            )
            return "HTTP " + response.code() + " " + response.message()
        }

        fun <T> checkNotNull(data: T?, message: String): T {
            if (data == null) {
                throw NullPointerException(message)
            }
            return data
        }
    }

    fun code(): Int {
        return code
    }

    fun message(): String {
        return statusMessage
    }

    fun response(): Response<*> {
        return response
    }
}


class EmptyBodyException(errorMessage: String? = null) : RuntimeException(errorMessage), TranslatableException {
    override fun getDefaultResourceId(): Int = R.string.error_empty_body
}

class NetworkUnavailableException(errorMessage: String? = null) : RuntimeException(errorMessage),
    TranslatableException {
    override fun getDefaultResourceId(): Int = R.string.error_no_internet
}

class TimeOutException(errorMessage: String? = null) : RuntimeException(errorMessage), TranslatableException {
    override fun getDefaultResourceId(): Int = R.string.error_network_timeout
}

class ParseDataException(errorMessage: String? = null) : RuntimeException(errorMessage), TranslatableException {
    override fun getDefaultResourceId(): Int = R.string.error_data_parse
}

class DefaultTransletableException(
    private val errorMessageId: Int = -1
) : RuntimeException(), TranslatableException {
    override fun getDefaultResourceId(): Int = if (errorMessageId != -1) errorMessageId else R.string.error_data_parse
}
