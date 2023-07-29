package com.bso112.roleplayai.android.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bso112.domain.ChatLog
import com.bso112.domain.ChatRepository
import com.bso112.domain.Profile
import com.bso112.domain.ProfileRepository
import com.bso112.roleplayai.android.util.DispatcherProvider
import com.bso112.roleplayai.android.util.stateIn
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val profileRepository: ProfileRepository,
    private val chatRepository: ChatRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    val profileList: StateFlow<List<Profile>> = stateIn(profileRepository.getProfiles())

    val chatLogList: StateFlow<List<ChatLog>> = stateIn(chatRepository.getChatLogDistinctByOpponentId())
    fun deleteProfile(profile: Profile) {
        viewModelScope.launch(dispatcherProvider.io) {
            launch { profileRepository.deleteProfile(profile) }
            launch { chatRepository.deleteChatLogByProfileId(profile.id) }
        }
    }

}


