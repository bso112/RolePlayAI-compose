package com.bso112.roleplayai.android.feature.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.bso112.domain.Profile
import com.bso112.domain.ProfileRepository
import com.bso112.roleplayai.android.util.DispatcherProvider
import com.bso112.roleplayai.android.util.logD
import com.bso112.roleplayai.android.util.logE
import com.bso112.roleplayai.android.util.randomID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class CreateProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val argument = CreateProfileArg(savedStateHandle)

    //프로필 수정인 경우의 프로필
    val profile: Profile? = argument.profile
    val name = MutableStateFlow(profile?.name.orEmpty())
    val description = MutableStateFlow(profile?.description.orEmpty())
    val profileImage = MutableStateFlow(profile?.thumbnail.orEmpty())


    suspend fun createProfile(context: Context) {
        val thumbnailUri = if (profileImage.value.isNotEmpty()) {
            val uri = Uri.parse(profileImage.value)
            val imageFile = File(context.filesDir, profile?.id ?: randomID)
            if (uri == Uri.fromFile(imageFile)) {
                profileImage.value
            } else {
                withContext(Dispatchers.IO) {
                    imageFile.createNewFile()
                }
                try {
                    context.contentResolver.openInputStream(uri)?.use { input ->
                        FileOutputStream(imageFile).use { output ->
                            input.copyTo(output)
                        }
                    }
                } catch (e: Exception) {
                    logE("uri: $uri, imageFile: $imageFile, exception $e")
                }
                Uri.fromFile(imageFile).toString()
            }
        } else {
            ""
        }

        logD("thumbnailUri $thumbnailUri")

        val profile = Profile(
            id = profile?.id ?: randomID,
            thumbnail = thumbnailUri,
            name = name.value,
            description = description.value
        )
        profileRepository.saveProfile(profile)
    }
}