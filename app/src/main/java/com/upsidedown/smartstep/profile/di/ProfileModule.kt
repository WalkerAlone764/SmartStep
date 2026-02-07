package com.upsidedown.smartstep.profile.di

import com.upsidedown.smartstep.profile.data.DataStoreProfileDataSource
import com.upsidedown.smartstep.profile.domain.ProfileDataSource
import com.upsidedown.smartstep.profile.presentation.setup.presentation.ProfileSetupViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val profileModule = module {
    factory<ProfileDataSource> { DataStoreProfileDataSource(get()) }

    viewModelOf(::ProfileSetupViewModel)
}