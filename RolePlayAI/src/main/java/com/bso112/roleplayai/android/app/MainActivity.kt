package com.bso112.roleplayai.android.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RolePlayAIApp(
                appState = rememberRolePlayAIAppState()
            )
        }
    }
}
