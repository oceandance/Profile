package com.jibergroup.di


import com.jibergroup.data.repositories.UserRepositoryImpl
import com.jibergroup.data.service.provideApiService
import com.jibergroup.domain.repositories.UserRepository
import org.koin.dsl.module

val dataModule = module {
    single { provideApiService() }

}

val repoModule = module {
    single<UserRepository> { UserRepositoryImpl(get()) }
}
