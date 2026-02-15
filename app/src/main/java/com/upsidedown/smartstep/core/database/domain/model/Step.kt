package com.upsidedown.smartstep.core.database.domain.model

import java.time.LocalDate

data class Step(
    val count: Int,
    val date: LocalDate
)