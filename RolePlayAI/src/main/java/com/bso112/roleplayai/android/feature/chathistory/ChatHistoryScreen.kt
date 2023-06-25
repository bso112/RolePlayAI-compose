package com.bso112.roleplayai.android.feature.chathistory

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.res.stringResource
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
import com.bso112.roleplayai.android.R
import com.bso112.roleplayai.android.app.RolePlayAppState
import com.bso112.roleplayai.android.app.placeHolder
import com.bso112.roleplayai.android.feature.chat.navigateChat
import com.bso112.roleplayai.android.util.DefaultPreview
import com.bso112.roleplayai.android.util.ifIs
import com.bso112.roleplayai.android.util.randomID
import org.koin.androidx.compose.koinViewModel

private enum class ChatHistoryEditOption(@StringRes val titleRes: Int) {
    DELETE(R.string.delete)
}

sealed class EditOptionDialogState(val isShow: Boolean) {
    class Open(val chatLog: ChatLog) : EditOptionDialogState(true)
    object Close : EditOptionDialogState(false)
}

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
    var dialogState: EditOptionDialogState by remember { mutableStateOf(EditOptionDialogState.Close) }

    LazyColumn {
        items(chatList) { chatLog ->
            ChatHistoryItem(
                chatLog = chatLog,
                onClickChatHistory = {
                    navController.navigateChat(
                        profileId = chatLog.profileId,
                        chatLogId = chatLog.id
                    )
                },
                onLongClickChatHistory = {
                    dialogState = EditOptionDialogState.Open(it)
                }
            )
        }
    }

    dialogState.ifIs<EditOptionDialogState.Open> { state ->
        EditOptionDialog(
            chatLogName = state.chatLog.name,
            onDismiss = {
                dialogState = EditOptionDialogState.Close
            },
            onClickOption = {
                dialogState = EditOptionDialogState.Close
            }
        )

    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ChatHistoryItem(
    chatLog: ChatLog,
    onClickChatHistory: (ChatLog) -> Unit,
    onLongClickChatHistory: (ChatLog) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .combinedClickable(
                onClick = { onClickChatHistory(chatLog) },
                onLongClick = { onLongClickChatHistory(chatLog) }
            )

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

@Composable
private fun EditOptionDialog(
    chatLogName: String,
    onClickOption: (ChatHistoryEditOption) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(chatLogName, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                ChatHistoryEditOption.values().forEach { option ->
                    TextButton(onClick = {
                        onClickOption(option)
                    }) {
                        Text(stringResource(id = option.titleRes))
                    }
                }
            }
        },
        confirmButton = {},
    )
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