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
import com.bso112.data.fakeCharacterList
import com.bso112.data.response.CharacterEntity
import com.bso112.roleplayai.android.app.RolePlayAppState
import com.bso112.roleplayai.android.app.placeHolder
import com.bso112.roleplayai.android.feature.chat.data.Character
import com.bso112.roleplayai.android.feature.chat.data.toUIModel
import com.bso112.roleplayai.android.util.DefaultPreview
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
    characterList: List<Character>,
    navController: NavController
) {
    Column {
        LazyColumn {
            items(characterList) {
                CharacterItem(character = it)
            }
        }
    }
}

@Composable
private fun CharacterItem(character: Character) {
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
            model = character.thumbnail,
            contentDescription = null,
            error = ColorPainter(MaterialTheme.colors.placeHolder),
            placeholder = ColorPainter(MaterialTheme.colors.placeHolder)
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text(character.name)
    }
}


@Preview
@Composable
private fun HomeScreenPreview() {
    DefaultPreview {
        HomeScreen(fakeCharacterList.map(CharacterEntity::toUIModel), rememberNavController())
    }
}

