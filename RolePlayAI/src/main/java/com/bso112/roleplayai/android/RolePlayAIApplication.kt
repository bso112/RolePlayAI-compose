package com.bso112.roleplayai.android

import android.app.Application
import com.bso112.roleplayai.android.feature.chat.ChatViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class RolePlayAIApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@RolePlayAIApplication)
            modules(appModule)
        }
    }
}

val appModule = module {
    viewModel { ChatViewModel() }
}