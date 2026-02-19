package com.vs.smartstep.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vs.smartstep.main.presentation.myprofile.MyProfileRoot
import com.vs.smartstep.main.presentation.smartstep.SmartStepHomeRoot


@Composable
fun NavigationApp(
    modifier: Modifier = Modifier, navController: NavHostController,
    isProfileSetup : Boolean = false

) {
    NavHost(
        navController = navController,
        startDestination = if(isProfileSetup)AppRoute.SmartStep else AppRoute.Profile,

        ) {


        composable<AppRoute.Profile> {
            MyProfileRoot(
                NavigatetoApp = {
                    navController.navigate(AppRoute.SmartStep){
                        popUpTo(AppRoute.Profile){
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<AppRoute.SmartStep> {
            SmartStepHomeRoot(
                onNavigatetoProfileScreen = {
                       navController.navigate(AppRoute.Profile){
                           launchSingleTop = true
                       }
                }

            )
        }



    }
}