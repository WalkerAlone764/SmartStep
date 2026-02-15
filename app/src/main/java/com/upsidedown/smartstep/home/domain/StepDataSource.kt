package com.upsidedown.smartstep.home.domain

import kotlinx.coroutines.flow.Flow

interface StepDataSource {
    fun listenSteps(): Flow<Long>

    suspend fun listenStep(): Long
}