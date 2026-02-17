package com.vs.smartstep.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vs.smartstep.main.presentation.myprofile.MyProfileRoot


@Composable
fun NavigationApp(
    modifier: Modifier = Modifier, navController: NavHostController

) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.Profile,

        ) {


        composable<AppRoute.Profile> {
            MyProfileRoot()
        }


    }
}