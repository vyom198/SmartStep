package com.vs.smartstep.main.di

import com.vs.smartstep.core.datastore.userProfileStoreImpl
import com.vs.smartstep.main.data.StepDetector
import com.vs.smartstep.main.domain.StepProvider
import com.vs.smartstep.main.domain.userProfileStore
import com.vs.smartstep.main.presentation.myprofile.MyProfileViewModel
import com.vs.smartstep.main.presentation.smartstep.SmartStepHomeViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val mainModule = module {
    viewModelOf(::MyProfileViewModel)
    viewModelOf(::SmartStepHomeViewModel)
   singleOf(::userProfileStoreImpl) bind userProfileStore :: class
    singleOf(::StepDetector) bind StepProvider :: class
}