package com.jibergroup.domain.usecases

import com.jibergroup.domain.entities.User
import com.jibergroup.domain.repositories.UserRepository
import com.jibergroup.domain.utils.RequestResult
import org.koin.core.KoinComponent
import org.koin.core.inject

class GetUsersUseCase : KoinComponent {

    private val userRepository: UserRepository by inject()

    suspend operator fun invoke(q: String): RequestResult<List<User>> {
        return userRepository.onSearchUsers(q)
    }
}