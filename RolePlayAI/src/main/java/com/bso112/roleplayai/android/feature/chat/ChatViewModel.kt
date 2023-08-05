package com.bso112.roleplayai.android.feature.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bso112.data.local.AppPreference
import com.bso112.domain.Chat
import com.bso112.domain.ChatLog
import com.bso112.domain.ChatRepository
import com.bso112.domain.Profile
import com.bso112.domain.ProfileRepository
import com.bso112.domain.Role
import com.bso112.domain.asPrompt
import com.bso112.domain.createChat
import com.bso112.roleplayai.android.R
import com.bso112.roleplayai.android.util.DispatcherProvider
import com.bso112.roleplayai.android.util.Empty
import com.bso112.roleplayai.android.util.replace
import com.bso112.roleplayai.android.util.stateIn
import com.bso112.util.randomID
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val profileRepository: ProfileRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val appPreference: AppPreference,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val argument = ChatScreenArg(savedStateHandle)
    val user: StateFlow<Profile> = stateIn(profileRepository.getUser(), Profile.Empty)
    val opponent: StateFlow<Profile> =
        stateIn(profileRepository.getProfile(argument.profileId), Profile.Empty)
    private val chatLogId: MutableStateFlow<String> =
        MutableStateFlow(argument.chatLogId ?: randomID)
    val chatLogList: StateFlow<List<ChatLog>> =
        stateIn(chatRepository.getChatLogByProfileId(argument.profileId))

    private val opponentAsFlow = opponent.filter { it != Profile.Empty }
    private val chatListAsFlow = chatLogId.flatMapLatest { chatRepository.getAllChat(it) }


    val chatList: StateFlow<List<Chat>> =
        opponentAsFlow.combine(chatListAsFlow) { opponent, chatList ->
            buildList {
                if (chatList.isEmpty()) {
                    add(opponent.createChat(opponent.firstMessage, chatLogId.value, Role.Assistant))
                }
                addAll(chatList)
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val userInput = MutableStateFlow("")

    private val coroutineContext =
        dispatcherProvider.io + CoroutineExceptionHandler { _, throwable ->
            viewModelScope.launch {
                _errorMessagesRes.emit(R.string.error_genetic)
            }
            _isSendingChat.update { false }
            throwable.printStackTrace()
        }

    private val _isSendingChat = MutableStateFlow(false)
    val isSendingChat = _isSendingChat.asStateFlow()

    private val _errorMessagesRes = MutableSharedFlow<Int>()
    val errorMessagesRes = _errorMessagesRes.asSharedFlow()

    fun sendChat(message: String) {
        viewModelScope.launch(coroutineContext) {
            val userChat =
                checkNotNull(user.value).createChat(message, chatLogId.value, Role.User)
            chatRepository.saveChatList(
                chatList.value + userChat,
                opponentName = opponent.value.name,
                opponentId = opponent.value.id
            )

            val requestChatList: List<Chat> = buildList {
                appPreference.mainPrompt.getValue().toPromptChat(role = Role.System).also(::add)

                CHARACTER_PROMPT.plus(opponent.value.description).toPromptChat(role = Role.System)
                    .also(::add)

                USER_PROMPT.plus(user.value.description).toPromptChat(role = Role.User).also(::add)

                addAll(chatList.value)
            }

            _isSendingChat.update { true }
            val chat = chatRepository.sendChat(
                speaker = checkNotNull(opponent.value),
                messages = requestChatList,
                logId = chatLogId.value
            ).first()

            chatRepository.saveChatList(
                chatList.value + chat,
                opponentName = opponent.value.name,
                opponentId = opponent.value.id
            )

            _isSendingChat.update { false }
        }
        userInput.update { String.Empty }
    }

    private fun String.toPromptChat(role: Role) = Chat(
        id = UUID.randomUUID().toString(),
        logId = UUID.randomUUID().toString(),
        profileId = UUID.randomUUID().toString(),
        thumbnail = "",
        name = "",
        message = this.asPrompt(
            userName = user.value.name,
            charName = opponent.value.name,
        ),
        role = role,
        createdAt = System.currentTimeMillis(),
    )

    fun translateChat(chat: Chat) {
        viewModelScope.launch(coroutineContext) {
            val translated =
                chatRepository.translateWithGPT(chat.message).first()
            val translatedChat = chat.copy(message = translated)
            chatRepository.saveChatList(
                chatList.value.replace(chat, translatedChat),
                opponentName = opponent.value.name,
                opponentId = opponent.value.id
            )
        }
    }

    fun changeLogId(logId: String) {
        chatLogId.update { logId }
    }

    fun deleteChatLogList(chatLogList: List<ChatLog>) {
        viewModelScope.launch(coroutineContext) {
            chatRepository.deleteChatLogList(chatLogList)
        }
    }

    companion object {
        private const val USER_PROMPT = "my name is {{user}}. here is my role: "
        private const val CHARACTER_PROMPT =
            "your name is {{char}}. here is your role: "
    }
}
