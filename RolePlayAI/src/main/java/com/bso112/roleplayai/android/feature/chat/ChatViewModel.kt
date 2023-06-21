package com.bso112.roleplayai.android.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bso112.domain.Chat
import com.bso112.domain.ChatRepository
import com.bso112.domain.Profile
import com.bso112.domain.ProfileRepository
import com.bso112.roleplayai.android.util.DispatcherProvider
import com.bso112.roleplayai.android.util.Empty
import com.bso112.roleplayai.android.util.addFirst
import com.bso112.roleplayai.android.util.randomID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID


@JvmInline
value class ChatLogId(val id: String) {
    constructor() : this(UUID.randomUUID().toString())
}

class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val profileRepository: ProfileRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val user = Profile(name = "유저", thumbnail = "", id = randomID)
    private val opponent = Profile(name = "상대", thumbnail = "", id = randomID)
    private val chatLogId = ChatLogId("0")

    private val _chatList: MutableStateFlow<List<Chat>> = MutableStateFlow(emptyList())
    val chatList: StateFlow<List<Chat>> = _chatList.asStateFlow()

    val userInput = MutableStateFlow("")

    init {
        viewModelScope.launch(dispatcherProvider.io) {
            chatRepository.getAllChat(chatLogId.id).onSuccess { chatLog ->
                _chatList.update { chatLog }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    fun sendChat(message: String) {
        _chatList.update {
            it.addFirst(
                Chat(
                    speaker = user,
                    message = message
                )
            )
        }
        viewModelScope.launch(dispatcherProvider.io) {
            chatRepository.sendChat(
                speaker = opponent,
                message = message
            ).onSuccess { chat ->
                _chatList.update { it.addFirst(chat) }
            }.onFailure {
                it.printStackTrace()
            }
        }
        userInput.update { String.Empty }
    }

    fun saveChatList() {
        viewModelScope.launch(dispatcherProvider.io) {
            chatRepository.saveChatList(logId = chatLogId.id, list = chatList.value).onFailure {
                it.printStackTrace()
            }
        }
    }
}