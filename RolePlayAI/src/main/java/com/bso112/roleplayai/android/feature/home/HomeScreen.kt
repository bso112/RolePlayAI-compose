package com.bso112.roleplayai.android.feature.home

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.bso112.domain.Profile
import com.bso112.roleplayai.android.R
import com.bso112.roleplayai.android.app.RolePlayAppState
import com.bso112.roleplayai.android.app.placeHolder
import com.bso112.roleplayai.android.feature.chat.navigateChat
import com.bso112.roleplayai.android.feature.profile.navigateCreateProfile
import com.bso112.roleplayai.android.util.DefaultPreview
import com.bso112.roleplayai.android.util.ifIs
import com.bso112.roleplayai.android.util.randomID
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
    val context = LocalContext.current

    HomeScreen(
        profileList,
        appState.navController,
        onDeleteProfile = { profile ->
            viewModel.deleteProfile(profile)
            profile.thumbnail.takeIf { it.isNotEmpty() }?.let(::File)?.delete()
        })
}

@Composable
private fun HomeScreen(
    profileList: List<Profile>,
    navController: NavController,
    onDeleteProfile: (Profile) -> Unit = {}
) {
    var profileActionDialogState: ProfileActionDialogState by remember {
        mutableStateOf(ProfileActionDialogState.Close)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(profileList) { profile ->
                ProfileItem(
                    profile = profile,
                    onProfileClick = { },
                    onProfileLongClick = {
                        profileActionDialogState = ProfileActionDialogState.Open(profile)
                    }
                )
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .combinedClickable(
                onLongClick = { onProfileLongClick() },
                onClick = { onProfileClick() }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(15.dp)),
            model = profile.thumbnail,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            error = ColorPainter(MaterialTheme.colors.placeHolder),
            placeholder = ColorPainter(MaterialTheme.colors.placeHolder)
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text(profile.name)
    }
}

@Composable
private fun ProfileActionDialog(
    profile: Profile,
    onClickOption: (ProfileLongClickAction) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(profile.name, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                ProfileLongClickAction.values().forEach { option ->
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
        HomeScreen(fakeProfileList, rememberNavController())
    }
}


private val fakeProfileList = List(20) {
    Profile(id = randomID, thumbnail = "", name = "Bot $it", description = "")
}
