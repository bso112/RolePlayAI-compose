package com.bso112.roleplayai.android.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bso112.data.RolePlayService
import com.bso112.roleplayai.android.feature.chat.data.Character
import com.bso112.roleplayai.android.feature.chat.data.Chat
import com.bso112.roleplayai.android.util.DispatcherProvider
import com.bso112.roleplayai.android.util.addFirst
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    private val service: RolePlayService,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    val user = Character(name = "유저", thumbnail = "")

    private val _chatList = MutableStateFlow(fakeChatData)
    val chatList: StateFlow<List<Chat>> = _chatList.asStateFlow()

    val userChat = MutableStateFlow("")

    fun sendChat(message: String) {
        _chatList.update {
            it.addFirst(
                Chat(
                    speaker = user,
                    content = message
                )
            )
        }
        viewModelScope.launch(dispatcherProvider.io) {
            service.sendChat(message).onSuccess { response ->
                _chatList.update {
                    it.addFirst(
                        Chat(
                            speaker = Character(thumbnail = "", name = "상대"),
                            content = response.choices.firstOrNull()?.message?.content.orEmpty()
                        )
                    )
                }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }
}