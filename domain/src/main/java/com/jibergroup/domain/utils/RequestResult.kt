package com.jibergroup.domain.utils

sealed class RequestResult<out T : Any?> {
    data class Success<out T : Any?>(val result: T? = null) : RequestResult<T>()
    data class Error(val error: String?, val code: Int = 0) : RequestResult<Nothing>(){
        var errorMessageId: Int? = null
    }
}