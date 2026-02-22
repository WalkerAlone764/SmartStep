package com.upsidedown.smartstep.home.domain

import kotlinx.coroutines.flow.Flow

interface StepCounterSettingDataSource {
    val isPaused: Flow<Boolean>
    suspend fun setPaused(isPaused: Boolean)
}
