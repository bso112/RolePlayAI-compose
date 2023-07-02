package com.bso112.roleplayai.android.feature.chat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalDrawer
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import coil.compose.AsyncImage
import com.bso112.domain.Chat
import com.bso112.domain.Profile
import com.bso112.domain.Role
import com.bso112.roleplayai.android.R
import com.bso112.roleplayai.android.app.RolePlayAITheme
import com.bso112.roleplayai.android.app.RolePlayAppState
import com.bso112.roleplayai.android.app.placeHolder
import com.bso112.roleplayai.android.util.DefaultPreview
import com.bso112.roleplayai.android.util.Empty
import com.bso112.roleplayai.android.util.fakeUser
import com.bso112.roleplayai.android.util.randomID
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
    val isSendingChat by viewModel.isSendingChat.collectAsStateWithLifecycle()

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val context = LocalContext.current

    LaunchedEffect(viewModel.errorMessagesRes) {
        viewModel.errorMessagesRes.flowWithLifecycle(lifecycle).collectLatest {
            viewModel.addSystemChat(name = context.getString(R.string.name_system), message = context.getString(it))
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
        })
}

@Composable
fun ChatScreen(
    chatList: List<Chat>,
    user: Profile,
    opponent: Profile,
    userChat: String,
    isSendingChat: Boolean,
    onClickBackButton: () -> Unit = {},
    onUserTextChanged: (String) -> Unit = {},
    onUserSubmitChat: (String) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val listState = rememberLazyListState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var selectedProfile: Profile by remember { mutableStateOf(Profile.Empty) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(chatList) {
        listState.animateScrollToItem(chatList.lastIndex.coerceAtLeast(0))
    }

    ChatDrawer(
        profile = selectedProfile,
        drawerState = drawerState,
    ) {
        Column {
            TopAppBar {
                IconButton(onClick = onClickBackButton) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "go back")
                }
                Text(opponent.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                    Icon(Icons.Filled.Menu, contentDescription = "go back")
                }
            }
            LazyColumn(state = listState, modifier = Modifier.weight(1f)) {
                items(chatList) { chat ->
                    ChatItem(
                        chat = chat,
                        onClickThumbnail = {
                            coroutineScope.launch {
                                val isUser = chat.profileId == user.id
                                selectedProfile = if (isUser) user else opponent
                                drawerState.open()
                            }
                        })
                }
            }
            Row(
                Modifier
                    .background(Color.LightGray),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = userChat,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                    ),
                    onValueChange = onUserTextChanged,
                    enabled = !isSendingChat,
                    modifier = Modifier
                        .padding(start = 15.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
                        .weight(1f),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        onUserSubmitChat(userChat)
                        focusManager.clearFocus()
                    })
                )
                if (isSendingChat) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(end = 15.dp)
                            .size(24.dp)
                    )
                } else {
                    IconButton(
                        modifier = Modifier
                            .padding(end = 15.dp)
                            .size(24.dp),
                        onClick = {
                            onUserSubmitChat(userChat)
                            focusManager.clearFocus()
                        }) {
                        Icon(Icons.Filled.Send, contentDescription = "send")
                    }
                }
            }

        }
    }
}

@Composable
fun ChatItem(
    chat: Chat,
    onClickThumbnail: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        AsyncImage(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(15.dp))
                .clickable { onClickThumbnail() },
            model = chat.thumbnail,
            contentScale = ContentScale.Crop,
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
                userChat = "",
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
    repeat(20) {
        add(
            Chat(
                name = "상대",
                thumbnail = "",
                id = randomID,
                message = "$it",
                profileId = randomID,
                logId = randomID,
                role = Role.Assistant
            )
        )
    }
}.reversed()

