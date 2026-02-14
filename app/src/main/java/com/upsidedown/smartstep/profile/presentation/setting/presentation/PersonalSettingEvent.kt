package com.upsidedown.smartstep.profile.presentation.setting.presentation

sealed interface PersonalSettingEvent {

    data object OnSuccessfullySave: PersonalSettingEvent
}