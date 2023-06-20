package com.bso112.roleplayai.android.feature.home

import androidx.lifecycle.ViewModel
import com.bso112.data.RolePlayService
import com.bso112.data.response.CharacterEntity
import com.bso112.roleplayai.android.feature.chat.data.Character
import com.bso112.roleplayai.android.feature.chat.data.toUIModel
import com.bso112.roleplayai.android.util.stateIn
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

class HomeViewModel(
    private val rolePlayService: RolePlayService
) : ViewModel() {

    val characterList: StateFlow<List<Character>> =
        stateIn(rolePlayService.getCharacters().map { it.map(CharacterEntity::toUIModel) })
}


