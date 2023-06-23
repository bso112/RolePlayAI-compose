package com.bso112.roleplayai.android.app

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.bso112.data.local.createDataBase
import com.bso112.data.local.datasource.ChatLocalDataSource
import com.bso112.data.local.datasource.ProfileLocalDataSource
import com.bso112.data.remote.datasource.ChatRemoteDataSource
import com.bso112.data.repository.ChatRepositoryImpl
import com.bso112.data.repository.ProfileRepositoryImpl
import com.bso112.domain.ChatRepository
import com.bso112.domain.ProfileRepository
import com.bso112.roleplayai.android.feature.chat.ChatViewModel
import com.bso112.roleplayai.android.feature.chathistory.ChatHistoryViewModel
import com.bso112.roleplayai.android.feature.home.HomeViewModel
import com.bso112.roleplayai.android.feature.profile.CreateProfileViewModel
import com.bso112.roleplayai.android.util.DispatcherProvider
import com.bso112.roleplayai.android.util.DispatcherProviderImpl
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class RolePlayAIApplication : Application() {

    private val appModule = module {
        single { createDataBase(this@RolePlayAIApplication) }
        single { ChatRemoteDataSource() }
        single { ChatLocalDataSource(get()) }
        single { ProfileLocalDataSource(get()) }
        single<ChatRepository> { ChatRepositoryImpl(get(), get()) }
        single<ProfileRepository> { ProfileRepositoryImpl(get()) }
        single<DispatcherProvider> { DispatcherProviderImpl }
        viewModel { HomeViewModel(get()) }
        viewModel { ChatHistoryViewModel(get()) }
        viewModel { (state: SavedStateHandle) -> ChatViewModel(get(), get(), get(), state) }
        viewModel { CreateProfileViewModel(get(), get()) }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@RolePlayAIApplication)
            modules(appModule)
        }
    }
}
