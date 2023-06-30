package com.bso112.roleplayai.android.feature.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import com.bso112.roleplayai.android.R
import com.bso112.roleplayai.android.app.RolePlayAppState
import com.bso112.roleplayai.android.util.DefaultPreview
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@Composable
fun CreateProfileScreenRoute(
    appState: RolePlayAppState,
    viewModel: CreateProfileViewModel = koinViewModel()
) {
    val name: String by viewModel.name.collectAsStateWithLifecycle()
    val description: String by viewModel.description.collectAsStateWithLifecycle()
    val profileImage: String by viewModel.profileImage.collectAsStateWithLifecycle(initialValue = viewModel.argument.profile?.thumbnail.orEmpty())

    val context = LocalContext.current
    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope

    val exceptionHandler = CoroutineExceptionHandler { _, t: Throwable ->
        t.printStackTrace()
    }


    CreateProfileScreen(
        profileImage = profileImage,
        name = name,
        description = description,
        onClickBackButton = {
            appState.navController.popBackStack()
        },
        onClickSubmit = {
            lifecycleScope.launch(exceptionHandler) {
                viewModel.createProfile(context)
                appState.navController.popBackStack()
            }
        },
        onNameChanged = viewModel.name::value::set,
        onDescriptionChanged = viewModel.description::value::set,
        onProfileImageChanged = viewModel.profileImage::value::set,
    )
}

@Composable
private fun CreateProfileScreen(
    profileImage: String,
    name: String,
    description: String,
    onClickBackButton: () -> Unit = {},
    onClickSubmit: () -> Unit = {},
    onNameChanged: (String) -> Unit = {},
    onDescriptionChanged: (String) -> Unit = {},
    onProfileImageChanged: (String) -> Unit = {},
) {

    val context = LocalContext.current

    val getContent =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.toString()?.let {
                onProfileImageChanged(it)
            }
        }


    Column {
        TopAppBar {
            IconButton(onClick = onClickBackButton) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "go back")
            }
            Spacer(Modifier.weight(1f))
            TextButton(onClick = onClickSubmit) {
                Text(
                    stringResource(id = R.string.submit),
                    fontSize = 16.sp
                )
            }
        }
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                modifier = Modifier
                    .size(100.dp)
                    .clickable { getContent.launch("image/*") },
                model = profileImage,
                placeholder = painterResource(id = R.drawable.saber),
                contentScale = ContentScale.Crop,
                error = ColorPainter(Color.Red),
                contentDescription = "portrait"
            )
            Spacer(modifier = Modifier.size(10.dp))
            Column {
                TextField(value = name, label = { Text("name") }, onValueChange = onNameChanged)
                TextField(
                    value = description,
                    label = { Text("description") },
                    onValueChange = onDescriptionChanged
                )
            }
        }
    }

}

@Composable
@Preview
private fun CreateProfileScreenPreView() {
    DefaultPreview {
        CreateProfileScreen(
            profileImage = "",
            name = "세이버",
            description = "영국의 기사왕"
        )
    }
}