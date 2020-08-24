package com.jibergroup.data.service.response

import com.google.gson.annotations.SerializedName

data class ListWrapper<out T>(
    @SerializedName("total_count")
    val totalCount: Int? = null,
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean? = false,
    val items : T? = null,
    val errors : List<ErrorWrapper>? = null
)

data class ErrorWrapper(
    val resource: String? = null,
    val field: String? = null,
    val code: String? = null
)