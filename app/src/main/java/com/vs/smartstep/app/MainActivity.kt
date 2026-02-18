package com.vs.smartstep.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.vs.smartstep.app.navigation.NavigationApp
import com.vs.smartstep.main.domain.userProfileStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge(
           statusBarStyle = SystemBarStyle.light(
               Color.Transparent.hashCode(),
               Color.Transparent.hashCode()
           ),
            navigationBarStyle = SystemBarStyle.light(
                Color.Transparent.hashCode(),
                Color.Transparent.hashCode()
            )

        )
        val userProfileStore : userProfileStore by inject()
        val coroutineScope : CoroutineScope by inject()
        var isProfileSetup = false
        coroutineScope.launch {
           userProfileStore.isProfileSetup().collect{ bool ->
               isProfileSetup = bool
               Timber.i(bool.toString())
           }
        }

        setContent {
            NavigationApp(
                  navController = rememberNavController(),
                  isProfileSetup = isProfileSetup
            )
        }
    }
}