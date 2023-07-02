package com.bso112.roleplayai.android.feature.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bso112.data.local.AppPreference
import com.bso112.domain.Chat
import com.bso112.domain.ChatRepository
import com.bso112.domain.Profile
import com.bso112.domain.ProfileRepository
import com.bso112.domain.Role
import com.bso112.domain.createChat
import com.bso112.domain.toChat
import com.bso112.domain.toChatLog
import com.bso112.roleplayai.android.R
import com.bso112.roleplayai.android.util.DispatcherProvider
import com.bso112.roleplayai.android.util.Empty
import com.bso112.roleplayai.android.util.randomID
import com.bso112.roleplayai.android.util.stateIn
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


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

    private val logId: String = (argument.chatLogId ?: randomID)

    val chatList: StateFlow<List<Chat>> =
        opponent.combine(chatRepository.getAllChat(logId)) { opponent, chatList ->
            buildList {
                add(opponent.createChat(opponent.firstMessage, logId, Role.Assistant))
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
            val userChat = checkNotNull(user.value).createChat(message, logId, Role.User)
            chatRepository.saveChat(userChat)
            chatRepository.saveChatLog(userChat.toChatLog(opponentId = opponent.value.id))

            val requestChatList: List<Chat> = buildList {
                appPreference.mainPrompt.getValue().toChat(
                    userName = user.value.name,
                    charName = opponent.value.name,
                    role = Role.System
                ).also(::add)

                CHARACTER_PROMPT.plus(opponent.value.description)
                    .toChat(
                        userName = user.value.name,
                        charName = opponent.value.name,
                        role = Role.System
                    ).also(::add)


                USER_PROMPT.plus(user.value.description)
                    .toChat(
                        userName = user.value.name,
                        charName = opponent.value.name,
                        role = Role.User
                    ).also(::add)


                addAll(chatList.value)
            }

            _isSendingChat.update { true }
            val chat = chatRepository.sendChat(
                speaker = checkNotNull(opponent.value),
                messages = requestChatList,
                logId = logId
            ).catch {
                _errorMessagesRes.emit(R.string.error_send_chat)
                it.printStackTrace()
                _isSendingChat.update { false }
            }.first()

            _isSendingChat.update { false }

            chatRepository.saveChat(chat = chat)
            chatRepository.saveChatLog(chat.toChatLog())
        }
        userInput.update { String.Empty }
    }

    companion object {
        private const val USER_PROMPT = "my name is {{user}}. here is my role: "
        private const val CHARACTER_PROMPT =
            "your name is {{char}}. here is your role: "
    }
}
