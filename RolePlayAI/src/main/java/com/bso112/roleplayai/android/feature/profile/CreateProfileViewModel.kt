package com.bso112.roleplayai.android.feature.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bso112.data.local.AppPreference
import com.bso112.domain.ChatRepository
import com.bso112.domain.Profile
import com.bso112.domain.ProfileRepository
import com.bso112.roleplayai.android.util.DispatcherProvider
import com.bso112.roleplayai.android.util.copyToFile
import com.bso112.roleplayai.android.util.logD
import com.bso112.util.randomID
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CreateProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val chatLogRepository: ChatRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val savedStateHandle: SavedStateHandle,
    private val appPreference: AppPreference
) : ViewModel() {

    val argument = CreateProfileArg(savedStateHandle)

    //프로필 수정인 경우의 프로필
    val profile: Profile? = argument.profile
    val name = MutableStateFlow(profile?.name.orEmpty())
    val singleLineDesc = MutableStateFlow(profile?.singleLineDesc.orEmpty())
    val description = MutableStateFlow(profile?.description.orEmpty())
    val profileImage = MutableStateFlow(profile?.thumbnail.orEmpty())
    val firstMessage = MutableStateFlow(profile?.firstMessage.orEmpty())

    val isUser = appPreference.user.map {
        profile?.id == it.id
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val _error = MutableSharedFlow<Throwable>()
    val error = _error.asSharedFlow()


    suspend fun createProfile(context: Context) = kotlin.runCatching {
        val profileId = profile?.id ?: randomID
        val currentImageUri = Uri.parse(profileImage.value)
        //content 스킴의 권한 문제 때문에 content 스킴이면 내부저장소에 카피한다.
        val thumbnailUri = if (currentImageUri.scheme == "content") {
            currentImageUri.copyToFile(context, fileName = profileId)
                .getOrNull()
                //file 스킴을 추가해준다.
                ?.let(Uri::fromFile)
                ?.toString()
                .orEmpty()
        } else {
            profileImage.value
        }

        logD("thumbnailUri $thumbnailUri")

        val newProfile = profile?.copy(
            id = profileId,
            thumbnail = thumbnailUri,
            name = name.value,
            description = description.value,
            firstMessage = firstMessage.value
        ) ?: Profile(
            id = profileId,
            thumbnail = thumbnailUri,
            name = name.value,
            description = description.value,
            firstMessage = firstMessage.value,
            singleLineDesc = singleLineDesc.value
        )

        coroutineScope {
            launch { profileRepository.saveProfile(newProfile) }
            launch { chatLogRepository.updateChatLogByProfile(newProfile) }
        }
    }.onFailure { t ->
        viewModelScope.launch {
            _error.emit(t)
        }
    }
}