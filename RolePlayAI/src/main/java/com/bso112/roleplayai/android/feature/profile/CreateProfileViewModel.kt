package com.bso112.roleplayai.android.feature.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.bso112.domain.Profile
import com.bso112.domain.ProfileRepository
import com.bso112.roleplayai.android.util.DispatcherProvider
import com.bso112.roleplayai.android.util.randomID
import kotlinx.coroutines.flow.MutableStateFlow

class CreateProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    //프로필 수정인 경우의 프로필
    val profile: Profile? = CreateProfileArg(savedStateHandle).profile
    val name = MutableStateFlow(profile?.name.orEmpty())
    val description = MutableStateFlow(profile?.description.orEmpty())
    val profileImage = MutableStateFlow(profile?.thumbnail.orEmpty())


    suspend fun createProfile() {
        val profile = Profile(
            id = profile?.id ?: randomID,
            thumbnail = profileImage.value,
            name = name.value,
            description = description.value
        )
        profileRepository.saveProfile(profile)
    }
}