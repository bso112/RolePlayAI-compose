package com.bso112.roleplayai.android.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import com.bso112.roleplayai.android.util.DefaultPreview
import com.bso112.roleplayai.android.util.randomID
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreenRoute(
    appState: RolePlayAppState,
    viewModel: HomeViewModel = koinViewModel()
) {
    val characterList by viewModel.characterList.collectAsStateWithLifecycle()

    HomeScreen(characterList, appState.navController)
}

@Composable
private fun HomeScreen(
    characterList: List<Profile>,
    navController: NavController
) {
    Column {
        LazyColumn {
            items(characterList) {
                CharacterItem(profile = it)
            }
        }
    }
}

@Composable
private fun CharacterItem(profile: Profile) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp),
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
    Profile(id = randomID, thumbnail = "", name = "Bot $it")
}