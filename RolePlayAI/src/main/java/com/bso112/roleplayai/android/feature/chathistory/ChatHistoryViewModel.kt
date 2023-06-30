package com.bso112.roleplayai.android.feature.chathistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bso112.domain.ChatLog
import com.bso112.domain.ChatRepository
import com.bso112.roleplayai.android.util.stateIn
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatHistoryViewModel(
    private val chatRepository: ChatRepository
) : ViewModel() {
    val chatList: StateFlow<List<ChatLog>> = stateIn(chatRepository.getChatLog())

    fun deleteChatLog(chatLog: ChatLog) {
        viewModelScope.launch {
            chatRepository.deleteChatLog(chatLog)
        }
    }
}