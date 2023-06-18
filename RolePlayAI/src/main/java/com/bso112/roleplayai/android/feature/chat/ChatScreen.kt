package com.bso112.roleplayai.android.feature.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.bso112.roleplayai.android.app.RolePlayAITheme
import com.bso112.roleplayai.android.feature.chat.data.Character
import com.bso112.roleplayai.android.feature.chat.data.Chat
import com.bso112.roleplayai.android.app.placeHolder
import kotlinx.coroutines.flow.update
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChatScreenRoute(
    viewModel: ChatViewModel = koinViewModel()
) {
    val chatList by viewModel.chatList.collectAsStateWithLifecycle()
    val userChat by viewModel.userChat.collectAsStateWithLifecycle()

    ChatScreen(
        chatList = chatList,
        userChat = userChat,
        onUserTextChanged = { newText ->
            viewModel.userChat.update { newText }
        },
        onUserSubmitChat = {
            viewModel.sendChat(it)
        })
}

@Composable
fun ChatScreen(
    chatList: List<Chat>,
    userChat: String,
    onUserTextChanged: (String) -> Unit = {},
    onUserSubmitChat: (String) -> Unit = {}
) {
    val navController = rememberNavController()
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            IconButton(onClick = { navController.navigateChat() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "go back")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(modifier = Modifier.weight(1f), reverseLayout = true) {
                items(chatList.size) {
                    ChatItem(chat = chatList[it])
                }
            }
            TextField(
                value = userChat,
                onValueChange = onUserTextChanged,
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    onUserSubmitChat(userChat)
                    focusManager.clearFocus()
                })
            )
        }
    }

}

@Composable
fun ChatItem(chat: Chat) {
    Row(Modifier.padding(15.dp)) {
        AsyncImage(
            modifier = Modifier.size(50.dp),
            model = chat.speaker.thumbnail,
            contentDescription = null,
            error = ColorPainter(MaterialTheme.colors.placeHolder),
            placeholder = ColorPainter(MaterialTheme.colors.placeHolder)
        )
        Column(modifier = Modifier.padding(start = 10.dp)) {
            Text(chat.speaker.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(chat.content)
        }
    }
}

@Preview
@Composable
fun ChatScreenPreView() {
    RolePlayAITheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background,
        ) {
            ChatScreen(fakeChatData, "")
        }
    }
}


val fakeChatData = buildList {
    repeat(20) {
        add(Chat(speaker = Character(name = "Saber", thumbnail = ""), content = "$it"))
    }
}.reversed()