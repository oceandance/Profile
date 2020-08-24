package com.jibergroup.gitprofile.di

import com.jibergroup.domain.usecases.GetUsersUseCase
import com.jibergroup.gitprofile.ui.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel {SearchViewModel(get())}
}

val useCasesModule = module {
    single { GetUsersUseCase() }
}