package com.bso112.roleplayai.android.app

import android.app.Application
import com.bso112.data.RolePlayService
import com.bso112.roleplayai.android.feature.chat.ChatViewModel
import com.bso112.roleplayai.android.feature.chathistory.ChatHistoryViewModel
import com.bso112.roleplayai.android.feature.home.HomeViewModel
import com.bso112.roleplayai.android.util.DispatcherProvider
import com.bso112.roleplayai.android.util.DispatcherProviderImpl
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
    single { RolePlayService() }
    single<DispatcherProvider> { DispatcherProviderImpl }
    viewModel { HomeViewModel(get()) }
    viewModel { ChatHistoryViewModel() }
    viewModel { ChatViewModel(get(), get()) }
}