package com.vs.smartstep.main.di

import com.vs.smartstep.core.datastore.userProfileStoreImpl
import com.vs.smartstep.main.data.smartstep.AIInsightService
import com.vs.smartstep.main.data.smartstep.ConnectionService
import com.vs.smartstep.main.data.smartstep.StepDetector
import com.vs.smartstep.main.domain.smartStep.AIInsights
import com.vs.smartstep.main.domain.smartStep.ConnectionProvider
import com.vs.smartstep.main.domain.smartStep.StepProvider
import com.vs.smartstep.main.domain.smartStep.userProfileStore
import com.vs.smartstep.main.presentation.chat.ChatViewModel
import com.vs.smartstep.main.presentation.myprofile.MyProfileViewModel
import com.vs.smartstep.main.presentation.smartstep.SmartStepHomeViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val mainModule = module {
    viewModelOf(::MyProfileViewModel)
    viewModelOf(::SmartStepHomeViewModel)
    viewModelOf(::ChatViewModel)
    singleOf(::ConnectionService) bind ConnectionProvider :: class
   singleOf(::userProfileStoreImpl) bind userProfileStore :: class
    singleOf(::StepDetector) bind StepProvider :: class
    singleOf(::AIInsightService) bind AIInsights :: class
}