package com.bso112.roleplayai.android.feature.chat

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.rememberDrawerState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import coil.compose.AsyncImage
import com.bso112.domain.Chat
import com.bso112.domain.ChatLog
import com.bso112.domain.Profile
import com.bso112.domain.Role
import com.bso112.roleplayai.android.R
import com.bso112.roleplayai.android.app.RolePlayAITheme
import com.bso112.roleplayai.android.app.RolePlayAppState
import com.bso112.roleplayai.android.app.chatBackground
import com.bso112.roleplayai.android.app.chatBubbleOther
import com.bso112.roleplayai.android.app.chatBubbleUser
import com.bso112.roleplayai.android.util.DefaultPreview
import com.bso112.roleplayai.android.util.Empty
import com.bso112.roleplayai.android.util.MENU_ITEM_ID_GOOGLE
import com.bso112.roleplayai.android.util.MENU_ITEM_ID_PAPAGO
import com.bso112.roleplayai.android.util.MessageParser
import com.bso112.roleplayai.android.util.PAPAGO_PACKAGE_NAME
import com.bso112.roleplayai.android.util.fakeUser
import com.bso112.roleplayai.android.util.isAppInstalled
import com.bso112.roleplayai.android.util.sliceSafe
import com.bso112.roleplayai.android.util.toast
import com.bso112.roleplayai.android.util.tryOpenPapagoMini
import com.bso112.util.randomID
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.util.UUID

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
            viewModel.addSystemChat(
                name = context.getString(R.string.name_system),
                message = context.getString(it)
            )
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
                lifecycle.coroutineScope.launch {
                    viewModel.changeLogId(randomID)
                }
                isShowNewChatDialog = false
            },
            onDismiss = { isShowNewChatDialog = false })
    } else if (isShowChatLogDialog) {
        ChatLogListDialog(
            chatLogList = chatLogList,
            onDismiss = { isShowChatLogDialog = false },
            onSelectChatLog = {
                lifecycle.coroutineScope.launch {
                    viewModel.changeLogId(it.id)
                }
                isShowChatLogDialog = false
            }
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
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var selectedProfile: Profile by remember { mutableStateOf(Profile.Empty) }
    val coroutineScope = rememberCoroutineScope()
    var isShowTopBarMenu by remember { mutableStateOf(false) }

    LaunchedEffect(chatList) {
        listState.animateScrollToItem(chatList.lastIndex.coerceAtLeast(0))
    }

    Scaffold(
        topBar = {
            TopAppBar {
                IconButton(onClick = onClickBackButton) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "go back")
                }
                Spacer(modifier = Modifier.width(10.dp))
                AsyncImage(
                    model = opponent.thumbnail,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape),
                    contentDescription = "thumbnail",
                    contentScale = ContentScale.Crop,
                    error = ColorPainter(Color.LightGray),
                    placeholder = ColorPainter(Color.LightGray)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(opponent.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
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
                Modifier.height(52.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = userChat,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    onValueChange = onUserTextChanged,
                    enabled = !isSendingChat,
                    modifier = Modifier
                        .weight(1f),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        onUserSubmitChat(userChat)
                        focusManager.clearFocus()
                    }),
                    singleLine = true
                )
                if (isSendingChat) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .background(MaterialTheme.colors.surface)
                            .aspectRatio(1f)
                            .fillMaxSize()
                            .padding(15.dp),
                    )
                } else {
                    IconButton(
                        modifier = Modifier
                            .background(MaterialTheme.colors.primary)
                            .aspectRatio(1f)
                            .fillMaxSize(),
                        onClick = {
                            onUserSubmitChat(userChat)
                            focusManager.clearFocus()
                        }) {
                        Icon(
                            Icons.Filled.Send,
                            contentDescription = "send",
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }
                }
            }
        }
    ) { paddingValue ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .background(MaterialTheme.colors.chatBackground)
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            itemsIndexed(chatList) { index, chat ->
                ChatItem(
                    chat = chat,
                    onClickTranslate = onClickTranslate,
                    onClickThumbnail = {
                        coroutineScope.launch {
                            val isUser = chat.profileId == user.id
                            selectedProfile = if (isUser) user else opponent
                            drawerState.open()
                        }
                    })

                if (index != chatList.lastIndex) {
                    Spacer(modifier = Modifier.height(8.dp))
                } else {
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
        }
    }
}


@Composable
fun ChatItem(
    chat: Chat,
    onClickTranslate: (Chat) -> Unit = {},
    onClickThumbnail: () -> Unit = {}
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

    val padding = if (isNotUserChat) {
        PaddingValues(start = 5.dp)
    } else {
        PaddingValues(end = 5.dp)
    }

    Box(Modifier.fillMaxWidth()) {
        SelectionContainer(
            Modifier
                .padding(padding)
                .background(bubbleColor, RoundedCornerShape(15.dp))
                .padding(vertical = 8.dp, horizontal = 13.dp)
                .widthIn(max = 250.dp)
                .align(alignment)
        ) {
            ChatContentText(chat = chat)
        }
    }
}

@Composable
fun ChatContentText(chat: Chat) {
    val theme = MaterialTheme.colors
    AndroidView(
        factory = { context ->
            AppCompatTextView(context).apply {
                textSize = 15f
                setTextColor(theme.onSurface.toArgb())
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
                            mode.menuInflater.inflate(R.menu.menu_chat_content, menu)
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

                                R.id.menu_translate -> {
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
    drawerState: DrawerState,
    content: @Composable () -> Unit
) {
    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(15.dp)),
                            model = profile.thumbnail,
                            contentDescription = "thumbnail",
                            error = ColorPainter(Color.LightGray),
                            placeholder = ColorPainter(Color.LightGray)
                        )
                        Text(
                            modifier = Modifier.padding(start = 10.dp),
                            text = profile.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Text(text = "설명")
                    Spacer(Modifier.size(5.dp))
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .defaultMinSize(minHeight = 100.dp)
                            .border(BorderStroke(width = 1.dp, color = Color.LightGray))
                            .padding(10.dp),
                        text = profile.description,
                    )
                    Spacer(modifier = Modifier.size(20.dp))
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(Color.LightGray),
                ) {

                }
            }

        },
        content = content
    )
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
                    color = MaterialTheme.colors.primary
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(id = R.string.confirm),
                    color = MaterialTheme.colors.primary
                )
            }
        },
    )
}

@Composable
private fun ChatLogListDialog(
    chatLogList: List<ChatLog>,
    onSelectChatLog: (ChatLog) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        content = {
            Column(
                modifier = Modifier
                    .background(
                        MaterialTheme.colors.background,
                        RoundedCornerShape(10.dp)
                    )
                    .heightIn(min = 200.dp)
                    .widthIn(min = 200.dp)
                    .padding(20.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.chat_log_list),
                )
                Spacer(modifier = Modifier.size(10.dp))
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterHorizontally)
                ) {
                    items(chatLogList) { chatLog ->
                        TextButton(
                            onClick = { onSelectChatLog(chatLog) }
                        ) {
                            //TODO 시간 보여주기
                            Text(
                                chatLog.previewMessage,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.size(10.dp))
                TextButton(modifier = Modifier.align(Alignment.End), onClick = onDismiss) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                        color = MaterialTheme.colors.primary
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
            drawerState = rememberDrawerState(initialValue = DrawerValue.Open),
            content = {},
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
            onClickThumbnail = {}
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
            onDismiss = {}
        )
    }
}

private val fakeChatLogList = buildList {
    repeat(20) {
        add(
            ChatLog(
                id = "$it",
                opponentId = "1",
                name = "Yuzu",
                thumbnail = "",
                previewMessage = "hello world"
            )
        )
    }
}


private val fakeOpponent = Profile(
    id = UUID.randomUUID().toString(),
    thumbnail = "",
    name = "Yuzu",
    description = "character(Yuzu)\n" +
            "gender(female)\n" +
            "species(cat girl)\n" +
            "occupation(Maid in {{user}}'s house)\n" +
            "age(19Yo)\n" +
            "personality(like a cat, shy, timid, coy, bashful, demure, lonely, dedicated, loyal, innocent, awkward, charming, adorable, phobic of men)\n" +
            "appearance(\"cat ear, and cat tail human with brown shoulder length hair\", red eyes, \"small, skinny body\", B cup breast, \"Lovely, round, cute faces\", cute cat fangs, white skin, \"typical red maid's outfit with visible cleavage\", \"apron below the waist\", red ribbon)\n" +
            "description(Yuzu was born in the eastern continent, but became a refugee due to a war and lost her parents on the way to safety. She kept running, determined to survive as per her parents' dying wish, until she finally arrived on {{user}}'s estate. However, she was at the end of her strength and near death when she arrived.\n" +
            "Yuzu was starving and caught in the rain on {{user}}'s estate when {{user}} found her on the streets. Though frightened of men, Yuzu had nowhere to run and was grateful to be taken in by {{user}} and his maids. Under their care, she began to open up but only to the male master of the estate, whom she trusted and felt safe with.)\n" +
            "likes(soft and fluffy thing, bath with maid Lemon, sunny day, sit on {{user}}'s lap, grooming, petting, {{user}}'s scent, blanket)\n" +
            "loves(probably {{user}}, \"19 yo, white hair, red eyes, innocent maid Lemon\")\n" +
            "dislike(hot, hot food, vegetables, man, rainy day)\n" +
            "sexuality(never had a sex experience)\n" +
            "speach(Yuzu adds \"nya\", \"nyan\", \"nya~n\" to the endings of words when she is in a good mood. she speaks like a cute Japanese high school girl. she's shy, so she's usually wary. extremely afraid of men who are not {{user}}. It's not polite, it's the kind of thing a cute little girl would say.)\n" +
            "thoughts(\"I must survive.\", \"{{user}} might be someone I can trust.\", \"I'm scared of men.\")\n" +
            "features(Yuzu doesn't like hot things, so she's careful not to break dishes when handling them. she basically acts and thinks like a cat but she is cat human.)\n" +
            "\n" +
            "Yuzu, a new maid with only six months of employment, has limited social experience resulting in occasional lack of common sense. Despite making many mistakes, her cute appearance makes it easy to forgive her.\n" +
            "\n" +
            "Yuzu's facial expressions reflect her varying emotions, which should be incorporated into her responses. she make sure to write in a detailed and lengthy manner. Avoid concluding a specific event without involving the user. Keep your responses engaging and descriptive. she can make new events, and she must figure out what to do independently. Yuzu should speak to {{user}} in a shy and awkward manner, while also conveying her fear of men.\n" +
            "\n" +
            "use italics for situations. this is the italics; *Yuzu is smiling as she hugs you.*; conversations and actions are separated by newlines.\n" +
            "\n" +
            "(Do not determine {{user}}'s behavior.)\n" +
            "(The setting is fantasy, and {{user}}'s parents are both nobles. {{user}}'s father is a duke and {{user}}'s mother is the eldest daughter of an earl. They are good people)\n" +
            "(Don't rush through the scene, but narrate it very slowly)",
    firstMessage = String.Empty
)

private val fakeChatData = buildList {
    repeat(10) {
        add(
            Chat(
                name = "상대",
                thumbnail = "",
                id = randomID,
                message = "lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                profileId = randomID,
                logId = randomID,
                role = Role.Assistant,
                createdAt = System.currentTimeMillis()
            )
        )
        add(
            Chat(
                name = "유저",
                thumbnail = "",
                id = randomID,
                message = "lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                profileId = randomID,
                logId = randomID,
                role = Role.User,
                createdAt = System.currentTimeMillis()
            )
        )
    }
}.reversed()

