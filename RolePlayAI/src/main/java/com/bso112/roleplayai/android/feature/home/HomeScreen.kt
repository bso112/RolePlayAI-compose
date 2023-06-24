package com.bso112.roleplayai.android.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.bso112.domain.Profile
import com.bso112.roleplayai.android.app.RolePlayAppState
import com.bso112.roleplayai.android.app.placeHolder
import com.bso112.roleplayai.android.data.toId
import com.bso112.roleplayai.android.feature.chat.navigateChat
import com.bso112.roleplayai.android.feature.profile.navigateCreateProfile
import com.bso112.roleplayai.android.util.DefaultPreview
import com.bso112.roleplayai.android.util.randomID
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreenRoute(
    appState: RolePlayAppState,
    viewModel: HomeViewModel = koinViewModel()
) {
    val profileList by viewModel.profileList.collectAsStateWithLifecycle()

    HomeScreen(profileList, appState.navController)
}

@Composable
private fun HomeScreen(
    profileList: List<Profile>,
    navController: NavController
) {
    Column {
        LazyColumn {
            items(profileList) {
                ProfileItem(profile = it) { profile ->
                    navController.navigateChat(profileId = profile.id.toId())
                }
            }
        }
        FloatingActionButton(onClick = {
            navController.navigateCreateProfile()
        }) {
            Icon(Icons.Filled.Add, contentDescription = "Add Profile")
        }
    }
}

@Composable
private fun ProfileItem(profile: Profile, onProfileClick: (Profile) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .clickable { onProfileClick(profile) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(15.dp)),
            model = profile.thumbnail,
            contentDescription = null,
            error = ColorPainter(MaterialTheme.colors.placeHolder),
            placeholder = ColorPainter(MaterialTheme.colors.placeHolder)
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text(profile.name)
    }
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
