package com.jibergroup.domain.repositories

import com.jibergroup.domain.entities.User
import com.jibergroup.domain.utils.RequestResult

interface UserRepository {

    suspend fun onSearchUsers(
        q: String,
        sort: String? = null,
        order: String? = null
    ): RequestResult<List<User>>

}