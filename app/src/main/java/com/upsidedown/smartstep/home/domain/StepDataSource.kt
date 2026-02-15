package com.upsidedown.smartstep.home.domain

interface StepDataSource {
    suspend fun listenSteps(): Long
}