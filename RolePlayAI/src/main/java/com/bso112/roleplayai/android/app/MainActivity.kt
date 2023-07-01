package com.bso112.roleplayai.android.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.bso112.data.local.AppPreference
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val appPreference: AppPreference by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RolePlayAIApp(
                appState = rememberRolePlayAIAppState(
                    appPreference = appPreference,
                    stateRetainedScope = lifecycleScope
                )
            )
        }
    }
}
