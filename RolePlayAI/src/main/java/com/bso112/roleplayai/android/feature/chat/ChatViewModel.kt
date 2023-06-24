package com.bso112.roleplayai.android.feature.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bso112.domain.Chat
import com.bso112.domain.ChatRepository
import com.bso112.domain.ProfileRepository
import com.bso112.domain.createChat
import com.bso112.roleplayai.android.util.DispatcherProvider
import com.bso112.roleplayai.android.util.Empty
import com.bso112.roleplayai.android.util.getUser
import com.bso112.roleplayai.android.util.randomID
import com.bso112.roleplayai.android.util.stateIn
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val profileRepository: ProfileRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val argument = ChatScreenArg(savedStateHandle)
    private val user = stateIn(profileRepository.getUser())
    private var opponent = stateIn(profileRepository.getProfile(argument.profileId))

    private val logId: String = (argument.chatLogId ?: randomID)

    val chatList: StateFlow<List<Chat>> =
        stateIn(chatRepository.getAllChat(logId))

    val userInput = MutableStateFlow("")

    private val coroutineContext =
        dispatcherProvider.io + CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }

    fun sendChat(message: String) {
        viewModelScope.launch(coroutineContext) {
            chatRepository.saveChat(
                chat = checkNotNull(user.value).createChat(message, logId)
            )

            val chat = chatRepository.sendChat(
                speaker = checkNotNull(opponent.value),
                message = message,
                logId = logId
            ).first()

            chatRepository.saveChat(chat = chat)
        }
        userInput.update { String.Empty }
    }
}
