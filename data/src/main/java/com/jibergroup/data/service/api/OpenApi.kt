package com.jibergroup.data.service.api

import com.jibergroup.data.service.response.ListWrapper
import com.jibergroup.domain.entities.User
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenApi {

    /**
     * @param(q) search string
     * @param(sort) - joined, followers or repositories
     * @param(order) - desc or asc (default desk)
     * */
    @GET("search/users")
    fun searchUser(
        @Query("q") q: String,
        @Query("sort") sort: String? = "joined",
        @Query("order") order: String? = "desc"
    ): Deferred<ListWrapper<List<User>>>

}