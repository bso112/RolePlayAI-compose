package com.bso112.roleplayai.android.util

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream


fun <T> CoroutineScope.stateIn(
    initialValue: T,
    sharingStarted: SharingStarted = SharingStarted.Eagerly,
    block: suspend () -> T
): StateFlow<T> = flow { emit(block()) }.stateIn(this, sharingStarted, initialValue)

fun <T> ViewModel.stateIn(
    flow: Flow<List<T>>,
    initialValue: List<T> = emptyList(),
    sharingStarted: SharingStarted = SharingStarted.Eagerly
): StateFlow<List<T>> = flow.stateIn(viewModelScope, sharingStarted, initialValue)

fun <T> ViewModel.stateIn(
    initialValue: T,
    sharingStarted: SharingStarted = SharingStarted.Eagerly,
    block: suspend () -> T
): StateFlow<T> = flow { emit(block()) }.stateIn(viewModelScope, sharingStarted, initialValue)


fun <T> ViewModel.stateIn(
    flow: Flow<T>,
    initialValue: T,
    sharingStarted: SharingStarted = SharingStarted.Eagerly
): StateFlow<T> = flow.stateIn(viewModelScope, sharingStarted, initialValue)


//Uri 컨텐츠를 내부 저장소에 저장한다.
suspend fun Uri.copyToFile(context: Context, fileName: String): Result<File> = kotlin.runCatching {
    val newFile = File(context.filesDir, fileName)

    withContext(Dispatchers.IO) {
        newFile.createNewFile()
    }

    context.contentResolver.openInputStream(this)?.use { input ->
        FileOutputStream(newFile).use { output ->
            input.copyTo(output)
        }
    }

    newFile
}

fun Context.isAppInstalled(packageName: String): Boolean {
    val packageManager = packageManager
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(
                packageName,
                PackageManager.PackageInfoFlags.of(0)
            )
        } else {
            packageManager.getPackageInfo(packageName, 0)
        }
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

fun Context.getPackageInfo(packageName: String): PackageInfo? {
    val packageManager = packageManager
    return kotlin.runCatching {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(
                packageName,
                PackageManager.PackageInfoFlags.of(PackageManager.GET_ACTIVITIES.toLong())
            )
        } else {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        }
    }.getOrNull()
}

fun Context.tryOpenPapagoMini(message: String): Boolean = kotlin.runCatching {
    Intent().apply {
        action = Intent.ACTION_SEND
        `package` = PAPAGO_PACKAGE_NAME
        component = ComponentName(
            PAPAGO_PACKAGE_NAME,
            PAPAGO_MINI_ACTIVITY_NAME
        )
        putExtra(Intent.EXTRA_TEXT, message)
        type = "text/plain"
    }.also {
        startActivity(it)
    }
    true
}.getOrDefault(false)

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
