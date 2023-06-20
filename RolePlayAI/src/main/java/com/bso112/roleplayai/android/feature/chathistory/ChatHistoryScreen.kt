package com.bso112.roleplayai.android.feature.chathistory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.bso112.roleplayai.android.app.RolePlayAppState
import com.bso112.roleplayai.android.app.placeHolder
import com.bso112.roleplayai.android.feature.chat.data.Chat
import com.bso112.roleplayai.android.feature.chat.fakeChatData
import com.bso112.roleplayai.android.feature.chat.navigateChat
import com.bso112.roleplayai.android.util.DefaultPreview
import org.koin.androidx.compose.koinViewModel


@Composable
fun ChatHistoryRoute(
    appState: RolePlayAppState,
    viewModel: ChatHistoryViewModel = koinViewModel()
) {
    val chatList by viewModel.chatList.collectAsStateWithLifecycle()

    ChatHistoryScreen(navController = appState.navController, chatList = chatList)
}

@Composable
private fun ChatHistoryScreen(
    navController: NavController,
    chatList: List<Chat>
) {
    LazyColumn {
        items(chatList) {
            ChatHistoryItem(chat = it) { navController.navigateChat() }
        }
    }
}

@Composable
private fun ChatHistoryItem(chat: Chat, onClickChatHistory: (Chat) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable { onClickChatHistory(chat) }
    ) {
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
private fun ChatHistoryPreview() {
    DefaultPreview {
        ChatHistoryScreen(navController = rememberNavController(), chatList = fakeChatData)
    }
}