package com.upsidedown.smartstep.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsidedown.smartstep.app.navigation.Routes
import com.upsidedown.smartstep.profile.domain.ProfileDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    private val profileDataSource: ProfileDataSource
): ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val isVisited = profileDataSource.isSetupVisited
        .flowOn(Dispatchers.IO)
    val state = _state
        .combine(isVisited) { state, isSetupVisited ->
            state.copy(
                startDestination = if (isSetupVisited) Routes.Home else Routes.ProfileSetup
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            MainState()
        )
}
