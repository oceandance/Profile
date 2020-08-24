package com.jibergroup.data.repositories

import com.jibergroup.data.service.api.OpenApi
import com.jibergroup.data.utils.*
import com.jibergroup.domain.entities.User
import com.jibergroup.domain.repositories.UserRepository
import com.jibergroup.domain.utils.RequestResult


class UserRepositoryImpl(
    private val openApi: OpenApi
) : UserRepository, CoroutineCaller by ApiCaller() {

    override suspend fun onSearchUsers(
        q: String,
        sort: String?,
        order: String?
    ): RequestResult<List<User>> {
        return coroutineApiCallWrapper(openApi.searchUser(q, sort, order))
    }
}