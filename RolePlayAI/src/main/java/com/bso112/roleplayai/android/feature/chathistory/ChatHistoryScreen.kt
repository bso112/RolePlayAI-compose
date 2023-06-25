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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.bso112.domain.ChatLog
import com.bso112.roleplayai.android.app.RolePlayAppState
import com.bso112.roleplayai.android.app.placeHolder
import com.bso112.roleplayai.android.feature.chat.navigateChat
import com.bso112.roleplayai.android.util.DefaultPreview
import com.bso112.roleplayai.android.util.randomID
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
    chatList: List<ChatLog>
) {
    LazyColumn {
        items(chatList) { chatLog ->
            ChatHistoryItem(chatLog = chatLog) {
                navController.navigateChat(
                    profileId = chatLog.profileId,
                    chatLogId = chatLog.id
                )
            }
        }
    }
}

@Composable
private fun ChatHistoryItem(chatLog: ChatLog, onClickChatHistory: (ChatLog) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable { onClickChatHistory(chatLog) }
    ) {
        AsyncImage(
            modifier = Modifier.size(50.dp),
            model = chatLog.thumbnail,
            contentDescription = null,
            error = ColorPainter(MaterialTheme.colors.placeHolder),
            placeholder = ColorPainter(MaterialTheme.colors.placeHolder)
        )
        Column(modifier = Modifier.padding(start = 10.dp, end = 20.dp)) {
            Text(chatLog.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(chatLog.previewMessage, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Preview
@Composable
private fun ChatHistoryPreview() {
    DefaultPreview {
        ChatHistoryScreen(navController = rememberNavController(), chatList = fakeChatLog)
    }
}

private val fakeChatLog = buildList {
    repeat(20) {
        add(
            ChatLog(
                name = "상대",
                thumbnail = "",
                previewMessage = "$it adaskd jaskldjas dasjdak lsdja klsjd asdkl jalsj dklajskldaklsdklaskldaklsdasdasd",
                id = randomID,
                profileId = randomID
            )
        )
    }
}