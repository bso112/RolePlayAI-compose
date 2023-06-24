package com.bso112.roleplayai.android.feature.profile.create

import androidx.lifecycle.ViewModel
import com.bso112.domain.Profile
import com.bso112.domain.ProfileRepository
import com.bso112.roleplayai.android.util.DispatcherProvider
import com.bso112.roleplayai.android.util.randomID
import kotlinx.coroutines.flow.MutableStateFlow

class CreateProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    val name = MutableStateFlow("")
    val description = MutableStateFlow("")


    suspend fun createProfile() {
        val profile = Profile(
            id = randomID,
            thumbnail = "",
            name = name.value,
            description = description.value
        )
        profileRepository.saveProfile(profile)
    }
}