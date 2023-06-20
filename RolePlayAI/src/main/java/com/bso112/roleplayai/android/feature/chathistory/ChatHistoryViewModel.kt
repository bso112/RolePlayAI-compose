package com.bso112.roleplayai.android.feature.chathistory

import androidx.lifecycle.ViewModel
import com.bso112.roleplayai.android.feature.chat.data.Chat
import com.bso112.roleplayai.android.feature.chat.fakeChatData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatHistoryViewModel : ViewModel() {
    private val _chatList = MutableStateFlow(fakeChatData)
    val chatList: StateFlow<List<Chat>> = _chatList.asStateFlow()
}