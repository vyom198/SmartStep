package com.vs.smartstep.main.di

import com.vs.smartstep.main.presentation.myprofile.MyProfileViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val mainModule = module {
    viewModelOf(::MyProfileViewModel)
}