package com.bso112.roleplayai.android.app

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.util.DebugLogger
import com.bso112.data.local.AppPreference
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

class RolePlayAIApplication : Application(), ImageLoaderFactory {

    private val appModule = module {
        single { AppPreference(this@RolePlayAIApplication) }
        single { createDataBase(this@RolePlayAIApplication, get()) }
        single { ChatRemoteDataSource(appPreference = get()) }
        single { ChatLocalDataSource(get()) }
        single { ProfileLocalDataSource(get()) }
        single<ChatRepository> { ChatRepositoryImpl(get(), get()) }
        single<ProfileRepository> { ProfileRepositoryImpl(get(), get()) }
        single<DispatcherProvider> { DispatcherProviderImpl }
        viewModel { HomeViewModel(get(), get(), get()) }
        viewModel { ChatHistoryViewModel(get()) }
        viewModel { (state: SavedStateHandle) -> ChatViewModel(get(), get(), get(), get(), state) }
        viewModel { (state: SavedStateHandle) -> CreateProfileViewModel(get(), get(), state) }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@RolePlayAIApplication)
            modules(appModule)
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .logger(DebugLogger())
            .build()
    }
}