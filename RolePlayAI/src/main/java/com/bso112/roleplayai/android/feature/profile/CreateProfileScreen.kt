package com.bso112.roleplayai.android.feature.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
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

    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope

    val exceptionHandler = CoroutineExceptionHandler { _ , t : Throwable ->
        t.printStackTrace()
    }

    CreateProfileScreen(
        name = name,
        description = description,
        onNameChanged = viewModel.name::value::set,
        onDescriptionChanged = viewModel.description::value::set,
        onClickCreateProfile = {
            lifecycleScope.launch(exceptionHandler) {
                viewModel.createProfile()
                appState.navController.popBackStack()
            }
        })
}

@Composable
private fun CreateProfileScreen(
    name: String,
    description: String,
    onNameChanged: (String) -> Unit = {},
    onDescriptionChanged: (String) -> Unit = {},
    onClickCreateProfile: () -> Unit = {}
) {
    Column {
        Row {
            Image(
                modifier = Modifier.size(100.dp),
                painter = ColorPainter(Color.LightGray),
                contentDescription = "portrait"
            )
            Column {
                TextField(value = name, label = { Text("name") }, onValueChange = onNameChanged)
                TextField(
                    value = description,
                    label = { Text("description") },
                    onValueChange = onDescriptionChanged
                )
            }
        }
        Button(onClickCreateProfile) {
            Text("Create")
        }
    }

}

@Composable
@Preview
private fun CreateProfileScreenPreView() {
    DefaultPreview {
        CreateProfileScreen(
            name = "세이버",
            description = "영국의 기사왕"
        )
    }
}