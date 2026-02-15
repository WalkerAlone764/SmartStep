package com.upsidedown.smartstep.core.database.data.mapper

import com.upsidedown.smartstep.core.database.data.entities.StepEntity
import com.upsidedown.smartstep.core.database.domain.model.Step

fun StepEntity.toStep(): Step {
    return Step(
        count = count,
        date = date
    )
}

fun Step.toStepEntity(): StepEntity {
    return StepEntity(
        count = count,
        date = date
    )
}