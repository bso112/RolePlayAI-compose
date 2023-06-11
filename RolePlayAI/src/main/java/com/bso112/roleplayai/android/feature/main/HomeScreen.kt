package com.bso112.roleplayai.android.feature.main

import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = koinViewModel()
) {
    HomeScreen()
}

@Composable
fun HomeScreen() {

}
