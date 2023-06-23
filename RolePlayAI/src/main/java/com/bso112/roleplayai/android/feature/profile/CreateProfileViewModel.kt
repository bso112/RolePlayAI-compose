package com.bso112.roleplayai.android.feature.profile

import androidx.lifecycle.ViewModel
import com.bso112.domain.Profile
import com.bso112.domain.ProfileRepository
import com.bso112.roleplayai.android.util.DispatcherProvider
import com.bso112.roleplayai.android.util.randomID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn

class CreateProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    val name = MutableStateFlow("")
    val description = MutableStateFlow("")


    suspend fun createProfile(onError: (Throwable) -> Unit) {
        val profile = Profile(
            id = randomID,
            thumbnail = "",
            name = name.value,
            description = description.value
        )
        profileRepository.saveProfile(profile)
            .catch { onError(it) }
            .flowOn(dispatcherProvider.io)
            .first()
    }
}