package com.bso112.roleplayai.android.feature.profile.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bso112.domain.Profile
import com.bso112.roleplayai.android.app.RolePlayAppState
import com.bso112.roleplayai.android.util.DefaultPreview
import com.bso112.roleplayai.android.util.fakeUser
import org.koin.androidx.compose.koinViewModel

enum class ProfileOption(
    val painter: Painter,
    val title: String
) {
    Talk(ColorPainter(Color.LightGray), "대화하기"),
    Edit(ColorPainter(Color.LightGray), "프로필 편집"),
    Delete(ColorPainter(Color.LightGray), "삭제하기")

}

@Composable
fun ProfileDetailRoute(
    appState: RolePlayAppState,
    viewModel: ProfileDetailViewModel = koinViewModel()
) {
    ProfileDetailScreen(viewModel.profile)
}

@Composable
private fun ProfileDetailScreen(
    profile: Profile
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Column {
                AsyncImage(
                    model = profile.thumbnail,
                    placeholder = ColorPainter(Color.LightGray),
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .size(60.dp),
                    contentDescription = "profile image"
                )
                Text(profile.name)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ProfileOption.values().map {
                    ProfileOptionItem(option = it)
                }
            }
        }

    }
}

@Composable
private fun ProfileOptionItem(option: ProfileOption, onClick: (ProfileOption) -> Unit = {}) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .clickable { onClick(option) },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier.size(50.dp),
            painter = option.painter,
            contentDescription = null
        )
        Spacer(Modifier.size(5.dp))
        Text(option.title)
    }
}


@Composable
@Preview
private fun ProfileDetailPreview() {
    DefaultPreview {
        ProfileDetailScreen(fakeUser)
    }
}