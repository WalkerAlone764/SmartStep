package com.upsidedown.smartstep.app

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsidedown.smartstep.app.navigation.Routes
import com.upsidedown.smartstep.profile.domain.ProfileDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    private val profileDataSource: ProfileDataSource
): ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state
        .combine(profileDataSource.isSetupVisited) { state, isSetupVisited ->
            Log.d("isSetup", isSetupVisited.toString())
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