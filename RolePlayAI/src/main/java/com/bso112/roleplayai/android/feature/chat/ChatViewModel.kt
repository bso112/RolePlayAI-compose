package com.bso112.roleplayai.android.feature.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bso112.domain.Chat
import com.bso112.domain.ChatRepository
import com.bso112.domain.Profile
import com.bso112.domain.ProfileRepository
import com.bso112.domain.createChat
import com.bso112.roleplayai.android.util.DispatcherProvider
import com.bso112.roleplayai.android.util.Empty
import com.bso112.roleplayai.android.util.addFirst
import com.bso112.roleplayai.android.util.getUser
import com.bso112.roleplayai.android.util.randomID
import com.bso112.roleplayai.android.util.stateIn
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val profileRepository: ProfileRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val user = stateIn(profileRepository.getUser())
    private var opponent: Profile? = null

    private val chatLogId: String? = savedStateHandle.get<String>(ARG_CHAT_LOG_ID)

    private val _chatList: MutableStateFlow<List<Chat>> = MutableStateFlow(emptyList())
    val chatList: StateFlow<List<Chat>> = _chatList.asStateFlow()

    val userInput = MutableStateFlow("")

    init {

        checkNotNull(savedStateHandle.get<String>(ARG_PROFILE_ID)).also {
            viewModelScope.launch {
                profileRepository.getProfile(it).collect {
                    opponent = it
                }
            }
        }

        chatLogId?.let { chatLogId ->
            viewModelScope.launch(dispatcherProvider.io) {
                chatRepository.getAllChat(chatLogId).onSuccess { chatLog ->
                    _chatList.update { chatLog }
                }.onFailure {
                    it.printStackTrace()
                }
            }
        }
    }

    fun sendChat(message: String) {
        _chatList.update {
            it.addFirst(checkNotNull(user.value).createChat(message))
        }
        viewModelScope.launch(dispatcherProvider.io) {
            chatRepository.sendChat(
                speaker = checkNotNull(opponent),
                message = message
            ).onSuccess { chat ->
                _chatList.update { it.addFirst(chat) }
            }.onFailure {
                it.printStackTrace()
            }
        }
        userInput.update { String.Empty }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun saveChatList() {
        /**
         * GlobalScope 내부에서 viewModel의 변수를 참조하면 메모리릭이 날 수도 있기에 얕은 복사를 한다.
         */
        val logId = chatLogId ?: randomID
        val chatList = chatList.value.toList()
        GlobalScope.launch(dispatcherProvider.io) {
            chatRepository.saveChatLog(logId = logId, list = chatList).onFailure {
                it.printStackTrace()
            }
        }
    }
}
