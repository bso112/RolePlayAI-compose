package com.bso112.roleplayai.android.feature.chathistory

import androidx.lifecycle.ViewModel
import com.bso112.domain.ChatLog
import com.bso112.domain.ChatRepository
import com.bso112.roleplayai.android.util.stateIn
import kotlinx.coroutines.flow.StateFlow

class ChatHistoryViewModel(
    private val chatRepository: ChatRepository
) : ViewModel() {
    val chatList: StateFlow<List<ChatLog>> = stateIn(chatRepository.getChatLog())
}