package com.upsidedown.smartstep.profile.domain

import kotlinx.coroutines.flow.Flow

interface ProfileDataSource {

    val profile: Flow<Profile?>

    val isSetupVisited: Flow<Boolean>


    suspend fun saveProfile(profile: Profile)

    suspend fun saveIsSetupVisited(isVisited: Boolean)
}