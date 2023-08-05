package com.bso112.roleplayai.android.feature.home

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
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
import com.bso112.data.toDateString
import com.bso112.domain.ChatLog
import com.bso112.domain.Profile
import com.bso112.roleplayai.android.R
import com.bso112.roleplayai.android.app.RolePlayAppState
import com.bso112.roleplayai.android.app.caption
import com.bso112.roleplayai.android.app.placeHolder
import com.bso112.roleplayai.android.app.subText
import com.bso112.roleplayai.android.fakeChatLogList
import com.bso112.roleplayai.android.feature.chat.navigateChat
import com.bso112.roleplayai.android.feature.profile.navigateCreateProfile
import com.bso112.roleplayai.android.util.DefaultPreview
import com.bso112.roleplayai.android.util.Empty
import com.bso112.roleplayai.android.util.ifIs
import com.bso112.util.randomID
import org.koin.androidx.compose.koinViewModel
import java.io.File

private enum class ProfileLongClickAction(@StringRes val titleRes: Int) {
    CHAT(R.string.chat), EDIT(R.string.edit), DELETE(R.string.delete)
}

private sealed interface ProfileActionDialogState {
    data class Open(val profile: Profile) : ProfileActionDialogState
    object Close : ProfileActionDialogState
}

@Composable
fun HomeScreenRoute(
    appState: RolePlayAppState,
    viewModel: HomeViewModel = koinViewModel()
) {
    val profileList by viewModel.profileList.collectAsStateWithLifecycle()
    val chatLogList by viewModel.chatLogList.collectAsStateWithLifecycle()

    HomeScreen(
        profileList = profileList,
        chatLogList = chatLogList,
        navController = appState.navController,
        onDeleteProfile = { profile ->
            viewModel.deleteProfile(profile)
            profile.thumbnail.takeIf { it.isNotEmpty() }?.let(::File)?.delete()
        })
}

@Composable
private fun HomeScreen(
    profileList: List<Profile>,
    chatLogList: List<ChatLog>,
    navController: NavController,
    onDeleteProfile: (Profile) -> Unit = {}
) {
    var profileActionDialogState: ProfileActionDialogState by remember {
        mutableStateOf(ProfileActionDialogState.Close)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primary)
        ) {
            Text(
                "Characters",
                modifier = Modifier.padding(
                    start = 15.dp,
                    end = 15.dp,
                    top = 20.dp,
                    bottom = 15.dp
                ),
                color = MaterialTheme.colors.onPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
            )
            if (profileList.isEmpty()) {
                Text(
                    text = "No characters yet",
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.padding(start = 15.dp, bottom = 10.dp),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                )
            } else {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(start = 15.dp)
                ) {
                    items(profileList) { profile ->
                        ProfileItem(
                            profile = profile,
                            onProfileClick = {
                                navController.navigateChat(profileId = profile.id)
                            },
                            onProfileLongClick = {
                                profileActionDialogState = ProfileActionDialogState.Open(profile)
                            }
                        )
                        Spacer(modifier = Modifier.size(15.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.size(15.dp))
            LazyColumn(
                modifier = Modifier
                    .background(
                        MaterialTheme.colors.surface,
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    )
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                item {
                    Spacer(modifier = Modifier.size(30.dp))
                }
                items(chatLogList) { chatLog ->
                    ChatLogItem(
                        chatLog = chatLog,
                        onChatLogClick = {
                            navController.navigateChat(
                                profileId = chatLog.opponentId,
                                chatLogId = chatLog.id
                            )
                        },
                        onChatLogLongClick = {

                        }
                    )
                }
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(30.dp),
            onClick = {
                navController.navigateCreateProfile()
            }) {
            Icon(Icons.Filled.Add, contentDescription = "Add Profile")
        }
    }
    profileActionDialogState.ifIs<ProfileActionDialogState.Open> { state ->
        ProfileActionDialog(
            profileActions = ProfileLongClickAction.values(),
            profile = state.profile,
            onClickOption = { action ->
                when (action) {
                    ProfileLongClickAction.CHAT -> {
                        navController.navigateChat(profileId = state.profile.id)
                    }

                    ProfileLongClickAction.EDIT -> {
                        navController.navigateCreateProfile(profile = state.profile)
                    }

                    ProfileLongClickAction.DELETE -> {
                        onDeleteProfile(state.profile)
                    }
                }
                profileActionDialogState = ProfileActionDialogState.Close
            },
            onDismiss = { profileActionDialogState = ProfileActionDialogState.Close })
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ProfileItem(
    profile: Profile,
    onProfileClick: () -> Unit,
    onProfileLongClick: () -> Unit
) {
    Column(
        modifier = Modifier.combinedClickable(
            onLongClick = { onProfileLongClick() },
            onClick = { onProfileClick() },
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            model = profile.thumbnail,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            error = ColorPainter(MaterialTheme.colors.placeHolder),
            placeholder = ColorPainter(MaterialTheme.colors.placeHolder)
        )
        Text(
            text = profile.name,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 5.dp),
            color = MaterialTheme.colors.onPrimary,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ChatLogItem(
    chatLog: ChatLog,
    onChatLogClick: () -> Unit,
    onChatLogLongClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onLongClick = { onChatLogLongClick() },
                onClick = { onChatLogClick() }
            )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 20.dp),
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.CenterVertically)
                    .clip(CircleShape),
                model = chatLog.thumbnail,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                error = ColorPainter(MaterialTheme.colors.placeHolder),
                placeholder = ColorPainter(MaterialTheme.colors.placeHolder)
            )
            Spacer(modifier = Modifier.size(10.dp))
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    chatLog.opponentName,
                    color = MaterialTheme.colors.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    chatLog.previewMessage,
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.subText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = chatLog.modifiedAt.toDateString(),
                fontSize = 12.sp,
                color = MaterialTheme.colors.caption,
            )
        }
    }
}

@Composable
private fun ProfileActionDialog(
    profileActions: Array<ProfileLongClickAction>,
    profile: Profile,
    onClickOption: (ProfileLongClickAction) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(profile.name, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                profileActions.forEach { option ->
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
private fun HomeScreenPreview() {
    DefaultPreview {
        HomeScreen(
            profileList = fakeProfileList,
            chatLogList = fakeChatLogList,
            navController = rememberNavController(),
            onDeleteProfile = {}
        )
    }
}


private val fakeProfileList = List(20) {
    Profile(
        id = randomID,
        thumbnail = "",
        name = "Bot $it",
        description = "",
        firstMessage = String.Empty,
        singleLineDesc = ""
    )
}
