package com.bso112.roleplayai.android.feature.chat

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ChatViewModel : ViewModel() {

    val userChat = mutableStateOf("")
}