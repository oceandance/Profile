package com.jibergroup.gitprofile

import android.app.Application
import com.jibergroup.di.dataModule
import com.jibergroup.di.repoModule
import com.jibergroup.gitprofile.di.useCasesModule
import com.jibergroup.gitprofile.di.viewModelsModule
import org.koin.core.context.startKoin

class MainApplication : Application(){

    override fun onCreate() {
        super.onCreate()

        //add koin modules
        startKoin {
            modules(listOf(dataModule, repoModule, useCasesModule, viewModelsModule))
        }

    }
}