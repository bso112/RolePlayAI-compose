package com.bso112.roleplayai.android.feature.home

import androidx.lifecycle.ViewModel
import com.bso112.domain.Profile
import com.bso112.domain.ProfileRepository
import com.bso112.roleplayai.android.util.stateIn
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    val characterList: StateFlow<List<Profile>> =
        stateIn(profileRepository.getProfiles())
}


