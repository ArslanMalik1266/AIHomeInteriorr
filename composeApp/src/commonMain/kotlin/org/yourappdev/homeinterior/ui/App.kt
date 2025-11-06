package org.yourappdev.homeinterior.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import homeinterior.composeapp.generated.resources.Res
import homeinterior.composeapp.generated.resources.compose_multiplatform
import org.yourappdev.homeinterior.Greeting
import org.yourappdev.homeinterior.navigation.Routes
import org.yourappdev.homeinterior.ui.BottomBarScreen.BaseBottomBarScreen
import org.yourappdev.homeinterior.ui.Create.CreateScreen
import org.yourappdev.homeinterior.ui.Files.CreateEditScreen
import org.yourappdev.homeinterior.ui.Files.FilesScreen
import org.yourappdev.homeinterior.ui.Generate.BaseAddScreen
import org.yourappdev.homeinterior.ui.OnBoarding.BaseScreen
import org.yourappdev.homeinterior.ui.OnBoarding.SplashScreen
import org.yourappdev.homeinterior.ui.theme.AppTypography

@Composable
@Preview
fun App() {

    val backStack = remember { mutableStateListOf<Any>(Routes.BaseAppScreen) }
    MaterialTheme(typography = AppTypography()) {
        BaseAddScreen()
//        NavDisplay(
//            backStack = backStack,
//            entryProvider = { key ->
//                when (key) {
//                    is Routes.OnBoarding -> NavEntry(key) {
//                        BaseScreen {
//                            backStack.add(Routes.BaseAppScreen)
//                        }
//                    }
//
//                    is Routes.BaseAppScreen -> NavEntry(key) {
//                        BaseBottomBarScreen()
//                    }
//
//                    else -> NavEntry(key) {
//                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
//                            Text("Nothing here!")
//                        }
//                    }
//                }
//
//            }
//        )
    }
}