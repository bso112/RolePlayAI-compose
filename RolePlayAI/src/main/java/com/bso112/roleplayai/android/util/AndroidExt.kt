package com.bso112.roleplayai.android.util

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import java.io.File
import java.io.FileOutputStream


fun <T> ViewModel.stateIn(
    flow: Flow<List<T>>,
    initialValue: List<T> = emptyList(),
    sharingStarted: SharingStarted = SharingStarted.Eagerly
) = flow.stateIn(viewModelScope, sharingStarted, initialValue)


fun <T> ViewModel.stateIn(
    flow: Flow<T>,
    initialValue: T,
    sharingStarted: SharingStarted = SharingStarted.Eagerly
) = flow.stateIn(viewModelScope, sharingStarted, initialValue)


/**
 * copy uri content to the file in [Context.getFilesDir].
 * create file if file of [fileName] is not exist.
 * @return file of [fileName]
 */
fun Uri.copyToFile(context: Context, file : File): File {
    logD(file.toString())
    kotlin.runCatching {
        if(!file.exists()){
            file.parentFile?.mkdirs()
            file.createNewFile()
        }
    }.getOrElse {
        logE("file $file errpr: $it")
    }

    context.contentResolver.openInputStream(this)?.use { input ->
        FileOutputStream(file).use { output ->
            input.copyTo(output)
        }
    }
    return file
}