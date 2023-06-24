package com.bso112.roleplayai.android.feature.profile.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.bso112.domain.Profile

class ProfileDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val profile: Profile = ProfileDetailArg(savedStateHandle).profile
}