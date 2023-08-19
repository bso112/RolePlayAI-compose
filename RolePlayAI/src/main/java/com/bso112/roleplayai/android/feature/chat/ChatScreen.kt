package com.bso112.roleplayai.android.feature.chat

import android.content.res.Configuration
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import coil.compose.AsyncImage
import com.bso112.domain.Chat
import com.bso112.domain.ChatLog
import com.bso112.domain.Profile
import com.bso112.domain.Role
import com.bso112.roleplayai.android.R
import com.bso112.roleplayai.android.app.RolePlayAITheme
import com.bso112.roleplayai.android.app.RolePlayAppState
import com.bso112.roleplayai.android.app.caption
import com.bso112.roleplayai.android.app.chatBubbleOther
import com.bso112.roleplayai.android.app.chatBubbleUser
import com.bso112.roleplayai.android.app.highlightText
import com.bso112.roleplayai.android.app.onChatBubbleOther
import com.bso112.roleplayai.android.app.onChatBubbleUser
import com.bso112.roleplayai.android.app.placeHolder
import com.bso112.roleplayai.android.fakeChatData
import com.bso112.roleplayai.android.fakeChatLogList
import com.bso112.roleplayai.android.fakeOpponent
import com.bso112.roleplayai.android.util.DefaultPreview
import com.bso112.roleplayai.android.util.MENU_ITEM_ID_GOOGLE
import com.bso112.roleplayai.android.util.MENU_ITEM_ID_PAPAGO
import com.bso112.roleplayai.android.util.MessageParser
import com.bso112.roleplayai.android.util.PAPAGO_PACKAGE_NAME
import com.bso112.roleplayai.android.util.fakeUser
import com.bso112.roleplayai.android.util.isAppInstalled
import com.bso112.roleplayai.android.util.sliceSafe
import com.bso112.roleplayai.android.util.toDateString
import com.bso112.roleplayai.android.util.toast
import com.bso112.roleplayai.android.util.tryOpenPapagoMini
import com.bso112.util.randomID
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChatScreenRoute(
    appState: RolePlayAppState,
    viewModel: ChatViewModel = koinViewModel()
) {
    val chatList by viewModel.chatList.collectAsStateWithLifecycle()
    val userChat by viewModel.userInput.collectAsStateWithLifecycle()
    val user by viewModel.user.collectAsStateWithLifecycle()
    val opponent by viewModel.opponent.collectAsStateWithLifecycle()
    val chatLogList by viewModel.chatLogList.collectAsStateWithLifecycle()
    val isSendingChat by viewModel.isSendingChat.collectAsStateWithLifecycle()
    var isShowNewChatDialog by remember {
        mutableStateOf(false)
    }
    var isShowChatLogDialog by remember {
        mutableStateOf(false)
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val context = LocalContext.current


    LaunchedEffect(viewModel.errorMessagesRes) {
        viewModel.errorMessagesRes.flowWithLifecycle(lifecycle).collectLatest {
            context.toast(context.getString(it))
        }
    }

    ChatScreen(
        chatList = chatList,
        userChat = userChat,
        isSendingChat = isSendingChat,
        user = user,
        opponent = opponent,
        onClickBackButton = {
            appState.navController.popBackStack()
        },
        onUserTextChanged = { newText ->
            viewModel.userInput.update { newText }
        },
        onUserSubmitChat = {
            viewModel.sendChat(it)
        },
        onClickTranslate = {
            viewModel.translateChat(it)
        },
        onSelectNewChatMenu = {
            isShowNewChatDialog = true
        },
        onSelectChatLogListMenu = {
            isShowChatLogDialog = true
        }
    )

    if (isShowNewChatDialog) {
        NewChatAlertDialog(
            onConfirm = {
                viewModel.changeLogId(randomID)
                isShowNewChatDialog = false
            },
            onDismiss = { isShowNewChatDialog = false })
    } else if (isShowChatLogDialog) {
        ChatLogListDialog(
            chatLogList = chatLogList,
            onDismiss = { isShowChatLogDialog = false },
            onSelectChatLog = {
                viewModel.changeLogId(it.id)
                isShowChatLogDialog = false
            },
            onEditChatLogAlias = {
                viewModel.updateChatLog(it)
            },
            onDeleteChatLog = {
                viewModel.deleteChatLog(it)
            },
        )
    }
}


@Composable
fun ChatScreen(
    chatList: List<Chat>,
    user: Profile,
    opponent: Profile,
    userChat: String,
    isSendingChat: Boolean,
    onClickTranslate: (Chat) -> Unit = {},
    onClickBackButton: () -> Unit = {},
    onUserTextChanged: (String) -> Unit = {},
    onUserSubmitChat: (String) -> Unit = {},
    onSelectNewChatMenu: () -> Unit = {},
    onSelectChatLogListMenu: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val listState = rememberLazyListState()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    var isShowTopBarMenu by remember { mutableStateOf(false) }
    var lastLazyColumnHeight by remember { mutableStateOf(0) }
    var lastChatCount: Int by remember { mutableStateOf(chatList.size) }


    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar {
                IconButton(onClick = onClickBackButton) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "go back")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable {
                            coroutineScope.launch {
                                scaffoldState.drawerState.open()
                            }
                        }) {
                    AsyncImage(
                        model = opponent.thumbnail,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape),
                        contentDescription = "thumbnail",
                        contentScale = ContentScale.Crop,
                        error = ColorPainter(MaterialTheme.colors.placeHolder),
                        placeholder = ColorPainter(MaterialTheme.colors.placeHolder)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        opponent.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Box {
                    IconButton(
                        modifier = Modifier
                            .size(42.dp)
                            .padding(end = 10.dp),
                        onClick = { isShowTopBarMenu = true }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "more options"
                        )
                    }
                    DropdownMenu(
                        expanded = isShowTopBarMenu,
                        onDismissRequest = { isShowTopBarMenu = false }) {
                        DropdownMenuItem(onClick = {
                            onSelectChatLogListMenu()
                            isShowTopBarMenu = false
                        }) {
                            Text(text = "채팅 목록")
                        }
                        DropdownMenuItem(onClick = {
                            onSelectNewChatMenu()
                            isShowTopBarMenu = false
                        }) {
                            Text(text = "새로운 채팅")
                        }
                    }
                }
            }
        },
        bottomBar = {
            Row(
                Modifier
                    .height(52.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .background(Color.White)
                        .weight(1f)
                ) {
                    TextField(
                        value = userChat,
                        colors = TextFieldDefaults.textFieldColors(
                            disabledTextColor = Color.Transparent,
                            backgroundColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        onValueChange = onUserTextChanged,
                        enabled = !isSendingChat,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            onUserSubmitChat(userChat)
                            focusManager.clearFocus()
                        }),
                        singleLine = true
                    )
                }
                if (isSendingChat) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .fillMaxSize()
                            .background(Color.LightGray)
                            .padding(15.dp),
                        color = Color.White
                    )
                } else {
                    val iconBackgroundColor = if (userChat.isNotEmpty()) {
                        MaterialTheme.colors.primary
                    } else {
                        Color.LightGray
                    }
                    IconButton(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .background(iconBackgroundColor)
                            .fillMaxSize(),
                        onClick = {
                            onUserSubmitChat(userChat)
                            focusManager.clearFocus()
                        }) {
                        Icon(
                            Icons.Filled.Send,
                            contentDescription = "send",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    ) { paddingValue ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface)
                .padding(paddingValue)
                .onSizeChanged { size ->
                    if (lastLazyColumnHeight == size.height) return@onSizeChanged
                    coroutineScope.launch {
                        if (listState.canScrollForward && listState.canScrollBackward && lastLazyColumnHeight != 0) {
                            listState.animateScrollBy(
                                lastLazyColumnHeight.toFloat() - size.height,
                                tween(durationMillis = 20)
                            )
                        }
                        lastLazyColumnHeight = size.height
                    }
                },
            contentPadding = PaddingValues(top = 30.dp)
        ) {
            itemsIndexed(chatList) { index, chat ->
                ChatItem(
                    chat = chat,
                    onClickTranslate = onClickTranslate
                )

                if (index != chatList.lastIndex) {
                    Spacer(modifier = Modifier.height(8.dp))
                } else {
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
        }
    }

    LaunchedEffect(chatList) {
        delay(10)
        val lasItem = listState.layoutInfo.visibleItemsInfo.lastOrNull() ?: return@LaunchedEffect
        coroutineScope.launch {
            listState.scrollToItem(chatList.size - 1, lasItem.size)
        }
    }
}


@Composable
fun ChatItem(
    chat: Chat,
    onClickTranslate: (Chat) -> Unit = {},
) {
    val isNotUserChat = chat.role == Role.Assistant || chat.role == Role.System
    val alignment = if (isNotUserChat) {
        Alignment.CenterStart
    } else {
        Alignment.CenterEnd
    }

    val bubbleColor = if (isNotUserChat) {
        MaterialTheme.colors.chatBubbleOther
    } else {
        MaterialTheme.colors.chatBubbleUser
    }

    val onBubbleColor = if (isNotUserChat) {
        MaterialTheme.colors.onChatBubbleOther
    } else {
        MaterialTheme.colors.onChatBubbleUser
    }

    val padding = if (isNotUserChat) {
        PaddingValues(start = 5.dp)
    } else {
        PaddingValues(end = 5.dp)
    }

    val bubbleShape = if (isNotUserChat) {
        RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp, bottomEnd = 15.dp)
    } else {
        RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp, bottomStart = 15.dp)
    }

    Box(Modifier.fillMaxWidth()) {
        SelectionContainer(
            Modifier
                .padding(padding)
                .background(bubbleColor, bubbleShape)
                .padding(vertical = 8.dp, horizontal = 13.dp)
                .widthIn(max = 250.dp)
                .align(alignment)
        ) {
            ChatContentText(chat = chat, textColor = onBubbleColor)
        }
    }
}

@Composable
fun ChatContentText(chat: Chat, textColor: Color) {
    AndroidView(
        factory = { context ->
            AppCompatTextView(context).apply {
                textSize = 15f
                setTextColor(textColor.toArgb())
                setTextIsSelectable(true)
                customSelectionActionModeCallback =
                    object : android.view.ActionMode.Callback {
                        override fun onCreateActionMode(
                            mode: android.view.ActionMode,
                            menu: Menu
                        ): Boolean {
                            if (context.isAppInstalled(PAPAGO_PACKAGE_NAME)) {
                                menu.add(
                                    Menu.NONE,
                                    MENU_ITEM_ID_PAPAGO,
                                    Menu.NONE,
                                    context.getString(R.string.menu_papago)
                                )
                            }
                            return true
                        }

                        override fun onPrepareActionMode(
                            mode: android.view.ActionMode?,
                            menu: Menu
                        ): Boolean {
                            //구글 번역, 구글 캘린더 추가 옵션을 제거한다.
                            menu.removeItem(MENU_ITEM_ID_GOOGLE)
                            return false
                        }

                        override fun onActionItemClicked(
                            mode: android.view.ActionMode?,
                            item: MenuItem
                        ): Boolean {
                            val selectedString: String =
                                text.sliceSafe(selectionStart..selectionEnd).toString()
                            return when (item.itemId) {
                                MENU_ITEM_ID_PAPAGO -> {
                                    if (!context.tryOpenPapagoMini(selectedString)) {
                                        context.toast(context.getString(R.string.fail_open_app))
                                    }
                                    mode?.finish()
                                    true
                                }
                                else -> false
                            }
                        }

                        override fun onDestroyActionMode(mode: android.view.ActionMode?) {}
                    }
            }
        },
        update = {
            // Updates view
            it.text = MessageParser.fromMessage(chat.message)
        },
    )
}

@Composable
fun ChatDrawer(
    profile: Profile,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 24.dp, end = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(15.dp)),
                model = profile.thumbnail,
                contentDescription = "thumbnail",
                error = ColorPainter(MaterialTheme.colors.placeHolder),
                placeholder = ColorPainter(MaterialTheme.colors.placeHolder)
            )
            Column(Modifier.padding(start = 15.dp), verticalArrangement = Arrangement.Center) {
                Text(
                    text = profile.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
                Text(
                    text = profile.singleLineDesc.ifEmpty { "No Description" },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.caption
                )
            }
        }
        Spacer(modifier = Modifier.size(30.dp))
        DrawerItem(
            icon = {
                Icon(
                    Icons.Outlined.Add,
                    contentDescription = "New Chat",
                    Modifier.size(25.dp),
                )
            },
            text = {
                Text(text = "New Chat")
            },
            onClickItem = {}
        )
        DrawerItem(
            icon = {
                Icon(
                    painterResource(id = R.drawable.history),
                    contentDescription = "History",
                    Modifier.size(25.dp)
                )
            },
            text = {
                Text(text = "History")
            },
            onClickItem = {}
        )
    }
}

@Composable
private fun DrawerItem(
    icon: @Composable RowScope.() -> Unit,
    text: @Composable RowScope.() -> Unit,
    onClickItem: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Spacer(modifier = Modifier.size(15.dp))
        text()
    }
}

@Composable
private fun NewChatAlertDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(
                text = stringResource(id = R.string.msg_start_new_chat),
                color = MaterialTheme.colors.onSurface
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    color = MaterialTheme.colors.highlightText
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(id = R.string.confirm),
                    color = MaterialTheme.colors.highlightText
                )
            }
        },
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ChatLogListDialog(
    chatLogList: List<ChatLog>,
    onSelectChatLog: (ChatLog) -> Unit,
    onDeleteChatLog: (ChatLog) -> Unit,
    onEditChatLogAlias: (ChatLog) -> Unit,
    onDismiss: () -> Unit
) {

    var isEditChatLogMode by remember { mutableStateOf(false) }
    var isEditChatLogAliasMode by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current

    LaunchedEffect(isEditChatLogMode, isEditChatLogAliasMode) {
        if (isEditChatLogAliasMode) {
            focusRequester.requestFocus()
        } else {
            focusRequester.freeFocus()
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        content = {
            Column(
                modifier = Modifier
                    .background(
                        MaterialTheme.colors.surface,
                        RoundedCornerShape(10.dp)
                    )
                    .heightIn(min = 200.dp)
                    .widthIn(min = 200.dp)
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.height(50.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = stringResource(id = R.string.chat_log_list))
                    Spacer(Modifier.weight(1f))
                    if (isEditChatLogMode) {
                        IconButton(onClick = {
                            isEditChatLogMode = false
                            isEditChatLogAliasMode = false
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "cancel selection mode"
                            )
                        }
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .heightIn(max = 500.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    items(chatLogList) { chatLog ->
                        var chatLogAlias by remember { mutableStateOf(TextFieldValue(chatLog.alias)) }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                Modifier
                                    .weight(1f)
                                    .padding(10.dp)
                                    .combinedClickable(
                                        onClick = { onSelectChatLog(chatLog) },
                                        onLongClick = { isEditChatLogMode = true }
                                    )
                            ) {
                                BasicTextField(
                                    modifier = Modifier
                                        .focusRequester(focusRequester),
                                    value = chatLogAlias,
                                    enabled = isEditChatLogAliasMode,
                                    onValueChange = { chatLogAlias = it },
                                    maxLines = 1,
                                    textStyle = TextStyle(
                                        color = MaterialTheme.colors.onSurface,
                                        fontSize = 16.sp
                                    ),
                                )
                                Text(
                                    modifier = Modifier
                                        .padding(top = 3.dp),
                                    text = chatLog.modifiedAt.toDateString(context),
                                    color = MaterialTheme.colors.caption,
                                    maxLines = 1,
                                    fontSize = 12.sp
                                )
                            }
                            AnimatedVisibility(visible = isEditChatLogMode) {
                                Row(
                                    modifier = Modifier.padding(start = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = {
                                            if (!isEditChatLogAliasMode) {
                                                isEditChatLogAliasMode = true
                                                chatLogAlias = chatLogAlias.copy(
                                                    selection = TextRange(chatLogAlias.text.length)
                                                )
                                            } else {
                                                isEditChatLogMode = false
                                                isEditChatLogAliasMode = false
                                                onEditChatLogAlias(chatLog.copy(alias = chatLogAlias.text))
                                            }
                                        }) {
                                        if (isEditChatLogAliasMode) {
                                            Icon(
                                                imageVector = Icons.Default.Done,
                                                contentDescription = "done edit chatlog"
                                            )
                                        } else {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "edit chatlog"
                                            )
                                        }
                                    }
                                    IconButton(onClick = { onDeleteChatLog(chatLog) }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "delete chatlog"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.size(10.dp))
                TextButton(modifier = Modifier.align(Alignment.End), onClick = onDismiss) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                        color = MaterialTheme.colors.highlightText
                    )
                }
            }
        }
    )
}


@Preview
@Composable
private fun ChatScreenPreView() {
    RolePlayAITheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background,
        ) {
            ChatScreen(
                chatList = fakeChatData,
                user = fakeUser,
                opponent = fakeOpponent,
                userChat = "hello world",
                isSendingChat = false
            )
        }
    }
}

@Preview
@Composable
private fun DrawerPreView() {
    DefaultPreview {
        ChatDrawer(
            profile = fakeOpponent
        )
    }
}

@Preview
@Composable
private fun ChatItemPreView() {
    DefaultPreview {
        ChatItem(
            chat = fakeChatData[0],
            onClickTranslate = {},
        )
    }
}

@Preview
@Composable
private fun NewChatDialogPreView() {
    DefaultPreview {
        NewChatAlertDialog(
            onConfirm = {},
            onDismiss = {}
        )
    }
}

@Preview
@Composable
private fun ChatLogListDialogPreView() {
    DefaultPreview {
        ChatLogListDialog(
            chatLogList = fakeChatLogList,
            onSelectChatLog = {},
            onDismiss = {},
            onDeleteChatLog = {},
            onEditChatLogAlias = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ChatScreenPreViewDark() {
    RolePlayAITheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background,
        ) {
            ChatScreen(
                chatList = fakeChatData,
                user = fakeUser,
                opponent = fakeOpponent,
                userChat = "hello world",
                isSendingChat = false
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DrawerPreViewDark() {
    DefaultPreview {
        ChatDrawer(
            profile = fakeOpponent
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ChatItemPreViewDark() {
    DefaultPreview {
        ChatItem(
            chat = fakeChatData[0],
            onClickTranslate = {},
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NewChatDialogPreViewDark() {
    DefaultPreview {
        NewChatAlertDialog(
            onConfirm = {},
            onDismiss = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ChatLogListDialogPreViewDark() {
    DefaultPreview {
        ChatLogListDialog(
            chatLogList = fakeChatLogList,
            onSelectChatLog = {},
            onDismiss = {},
            onDeleteChatLog = {},
            onEditChatLogAlias = {}
        )
    }
}

