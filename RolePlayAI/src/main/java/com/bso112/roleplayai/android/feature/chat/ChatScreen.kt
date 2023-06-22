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
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import coil.compose.AsyncImage
import com.bso112.domain.Chat
import com.bso112.roleplayai.android.app.RolePlayAITheme
import com.bso112.roleplayai.android.app.RolePlayAppState
import com.bso112.roleplayai.android.app.placeHolder
import com.bso112.roleplayai.android.util.randomID
import kotlinx.coroutines.flow.update
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChatScreenRoute(
    appState: RolePlayAppState,
    viewModel: ChatViewModel = koinViewModel()
) {
    val chatList by viewModel.chatList.collectAsStateWithLifecycle()
    val userChat by viewModel.userInput.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        onDispose {
            viewModel.saveChatList()
        }
    }

    ChatScreen(
        chatList = chatList,
        userChat = userChat,
        onClickBackButton = {
            appState.navController.popBackStack()
        },
        onUserTextChanged = { newText ->
            viewModel.userInput.update { newText }
        },
        onUserSubmitChat = {
            viewModel.sendChat(it)
        })
}

@Composable
fun ChatScreen(
    chatList: List<Chat>,
    userChat: String,
    onClickBackButton: () -> Unit = {},
    onUserTextChanged: (String) -> Unit = {},
    onUserSubmitChat: (String) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    Column {
        TopAppBar {
            IconButton(onClick = onClickBackButton) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "go back")
            }
        }
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

@Composable
fun ChatItem(chat: Chat) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        AsyncImage(
            modifier = Modifier.size(50.dp),
            model = chat.thumbnail,
            contentDescription = null,
            error = ColorPainter(MaterialTheme.colors.placeHolder),
            placeholder = ColorPainter(MaterialTheme.colors.placeHolder)
        )
        Column(modifier = Modifier.padding(start = 10.dp)) {
            Text(chat.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(chat.message)
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
        add(Chat(name = "상대", thumbnail = "", id = randomID, message = "$it", profileId = randomID))
    }
}.reversed()