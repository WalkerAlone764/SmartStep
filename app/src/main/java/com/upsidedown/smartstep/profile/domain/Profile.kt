package com.upsidedown.smartstep.profile.domain

import com.upsidedown.smartstep.core.presentation.util.Gender

data class Profile(
    val gender: Gender,
    val height: Int,
    val weight: Int,
    val heightUnit: HeightUnit,
    val weightUnit: WeightUnit
)

enum class HeightUnit {
    CM, FT_IN
}

enum class WeightUnit {
    KG, LBS
}
