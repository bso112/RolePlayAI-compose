package com.bso112.roleplayai.android.feature.profile

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import com.bso112.roleplayai.android.R
import com.bso112.roleplayai.android.app.RolePlayAppState
import com.bso112.roleplayai.android.app.placeHolder
import com.bso112.roleplayai.android.util.DefaultPreview
import com.bso112.roleplayai.android.util.logD
import com.bso112.roleplayai.android.util.logE
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
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
    val singleLineDesc: String by viewModel.singleLineDesc.collectAsStateWithLifecycle()
    val profileImage: String by viewModel.profileImage.collectAsStateWithLifecycle(initialValue = viewModel.argument.profile?.thumbnail.orEmpty())
    val firstMessage: String by viewModel.firstMessage.collectAsStateWithLifecycle()
    val isUser: Boolean by viewModel.isUser.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope

    val exceptionHandler = CoroutineExceptionHandler { _, t: Throwable ->
        t.printStackTrace()
    }


    CreateProfileScreen(
        profileImage = profileImage,
        name = name,
        description = description,
        firstMessage = firstMessage,
        singleLineDesc = singleLineDesc,
        isUser = isUser,
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
        onSingleLineDescriptionChanged = viewModel.singleLineDesc::value::set,
        onProfileImageChanged = viewModel.profileImage::value::set,
        onFirstMessageChanged = viewModel.firstMessage::value::set,
    )
}

@Composable
private fun CreateProfileScreen(
    profileImage: String,
    name: String,
    isUser: Boolean,
    description: String,
    singleLineDesc: String,
    firstMessage: String,
    onClickBackButton: () -> Unit = {},
    onClickSubmit: () -> Unit = {},
    onNameChanged: (String) -> Unit = {},
    onDescriptionChanged: (String) -> Unit = {},
    onSingleLineDescriptionChanged: (String) -> Unit = {},
    onProfileImageChanged: (String) -> Unit = {},
    onFirstMessageChanged: (String) -> Unit = {},
) {

    val imageChopper =
        rememberLauncherForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                logD(result.uriContent.toString())
                onProfileImageChanged(result.uriContent.toString())
            } else {
                logE(result.error?.stackTraceToString().orEmpty())
            }
        }


    val getContent =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageChopper.launch(
                    CropImageContractOptions(
                        uri = it, cropImageOptions = CropImageOptions(
                            guidelines = CropImageView.Guidelines.ON,
                            fixAspectRatio = true,
                            maxZoom = 2,
                            aspectRatioX = 1,
                            aspectRatioY = 1,
                        )
                    )
                )
            }
        }

    val themeColors = MaterialTheme.colors

    Scaffold(
        topBar = {
            TopAppBar {
                IconButton(onClick = onClickBackButton) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "go back")
                }
                Spacer(Modifier.weight(1f))
                TextButton(onClick = onClickSubmit) {
                    Text(
                        text = stringResource(id = R.string.submit),
                        fontSize = 16.sp,
                        color = themeColors.onPrimary
                    )
                }
            }
        }
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .padding(paddingValue)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                AsyncImage(
                    modifier = Modifier
                        .size(100.dp)
                        .clickable { getContent.launch("image/*") }
                        .clip(CircleShape),
                    model = profileImage,
                    placeholder = ColorPainter(MaterialTheme.colors.placeHolder),
                    contentScale = ContentScale.Crop,
                    error = ColorPainter(MaterialTheme.colors.placeHolder),
                    contentDescription = "portrait"
                )
                IconButton(
                    onClick = { getContent.launch("image/*") },
                    modifier = Modifier
                        .shadow(2.dp, CircleShape)
                        .background(Color.White, shape = CircleShape)
                        .align(Alignment.BottomEnd)
                        .size(30.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(id = R.drawable.add_photo2),
                        tint = MaterialTheme.colors.primary,
                        contentDescription = "add photo"
                    )
                }
            }
            Spacer(modifier = Modifier.size(10.dp))
            Column(Modifier.padding(horizontal = 10.dp)) {
                ProfileInput(
                    title = stringResource(id = R.string.profile_name),
                    value = name,
                    onValueChange = onNameChanged
                )
                Spacer(modifier = Modifier.size(10.dp))
                ProfileInput(
                    title = stringResource(id = R.string.profile_single_line_description),
                    value = singleLineDesc,
                    onValueChange = onSingleLineDescriptionChanged
                )
                Spacer(modifier = Modifier.size(10.dp))
                ProfileInput(
                    title = stringResource(id = R.string.profile_description),
                    value = description,
                    textFieldBoxHeight = 200.dp,
                    onValueChange = onDescriptionChanged
                )
                Spacer(modifier = Modifier.size(10.dp))
                if (!isUser) {
                    ProfileInput(
                        title = stringResource(id = R.string.profile_first_message),
                        value = firstMessage,
                        onValueChange = onFirstMessageChanged
                    )
                }
                Spacer(modifier = Modifier.size(20.dp))
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colors.primary,
                            shape = RoundedCornerShape(5.dp)
                        ),
                    onClick = onClickSubmit
                ) {
                    Text(
                        text = stringResource(id = R.string.btn_submit),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onPrimary,
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileInput(
    title: String,
    value: String,
    textFieldBoxHeight: Dp? = null,
    onValueChange: (String) -> Unit = {}
) {
    val modifier = if (textFieldBoxHeight == null) {
        Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, RoundedCornerShape(5.dp))
    } else {
        Modifier
            .fillMaxWidth()
            .height(textFieldBoxHeight)
            .border(1.dp, Color.LightGray, RoundedCornerShape(5.dp))
    }
    Text(title, fontWeight = FontWeight.Bold, color = MaterialTheme.colors.onSurface)
    Spacer(modifier = Modifier.size(5.dp))
    Box(
        modifier = modifier
    ) {
        BasicTextField(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            value = value,
            textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
            onValueChange = onValueChange,
            cursorBrush = SolidColor(MaterialTheme.colors.primary),
        )
    }
}

@Composable
@Preview
private fun CreateProfileScreenPreView() {
    DefaultPreview {
        CreateProfileScreen(
            isUser = false,
            firstMessage = "안녕하세요",
            name = "세이버",
            description = "영국의 기사왕",
            profileImage = "그대가 나의 마스터인가?",
            singleLineDesc = "영국의 기사왕",
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun CreateProfileScreenNightPreView() {
    DefaultPreview {
        CreateProfileScreen(
            isUser = false,
            firstMessage = "안녕하세요",
            name = "세이버",
            description = "영국의 기사왕",
            profileImage = "그대가 나의 마스터인가?",
            singleLineDesc = "영국의 기사왕",
        )
    }
}