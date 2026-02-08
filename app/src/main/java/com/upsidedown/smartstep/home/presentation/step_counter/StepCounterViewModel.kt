package com.upsidedown.smartstep.home.presentation.step_counter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class StepCounterViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(StepCounterState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = StepCounterState()
        )

    private val _event = Channel<StepCounterEvent>()
    val event = _event.receiveAsFlow()

    fun onAction(action: StepCounterAction) {
        when (action) {
            StepCounterAction.OnClickExit -> {
                _state.update { it.copy(isExitDialogShown = true) }
            }

            StepCounterAction.OnClickOk -> onClickOk()
            StepCounterAction.OnDismissCloseDialog -> onDismissCloseDialog()
        }
    }

    private fun onClickOk() {
        _event.trySend(StepCounterEvent.ExitApp)
    }

    private fun onDismissCloseDialog() {
        _state.update { it.copy(isExitDialogShown = false) }
    }

}