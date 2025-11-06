package org.yourappdev.homeinterior.ui.BottomBarScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import homeinterior.composeapp.generated.resources.Res
import homeinterior.composeapp.generated.resources.add_2_24px
import homeinterior.composeapp.generated.resources.createiconfinal
import homeinterior.composeapp.generated.resources.iconfiles
import homeinterior.composeapp.generated.resources.newexploreicon
import homeinterior.composeapp.generated.resources.profileiconnew
import homeinterior.composeapp.generated.resources.selectedcreate
import homeinterior.composeapp.generated.resources.selectedexploreicon
import homeinterior.composeapp.generated.resources.selectedfile
import homeinterior.composeapp.generated.resources.selectedprofile
import homeinterior.composeapp.generated.resources.tabFourProfile
import homeinterior.composeapp.generated.resources.tabOneCreate
import homeinterior.composeapp.generated.resources.tabThreeFiles
import homeinterior.composeapp.generated.resources.tabTwoExplore
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.yourappdev.homeinterior.navigation.Routes
import org.yourappdev.homeinterior.ui.Account.AccountScreen
import org.yourappdev.homeinterior.ui.Account.ProfileScreen
import org.yourappdev.homeinterior.ui.Account.SubscriptionScreen
import org.yourappdev.homeinterior.ui.Create.CreateScreen
import org.yourappdev.homeinterior.ui.Explore.ExploreScreen
import org.yourappdev.homeinterior.ui.Files.CreateEditScreen
import org.yourappdev.homeinterior.ui.Files.FilesScreen
import org.yourappdev.homeinterior.ui.OnBoarding.BaseScreen
import org.yourappdev.homeinterior.ui.UiUtils.SlippyBar
import org.yourappdev.homeinterior.ui.UiUtils.SlippyBarStyle
import org.yourappdev.homeinterior.ui.UiUtils.SlippyBottomBar
import org.yourappdev.homeinterior.ui.UiUtils.SlippyIconStyle
import org.yourappdev.homeinterior.ui.UiUtils.SlippyOptions
import org.yourappdev.homeinterior.ui.UiUtils.SlippyTab
import org.yourappdev.homeinterior.ui.UiUtils.SlippyTextStyle
import org.yourappdev.homeinterior.ui.theme.bottomBarBack
import org.yourappdev.homeinterior.ui.theme.selectedNavItem
import org.yourappdev.homeinterior.ui.theme.unselectedNavItem

@Composable
fun BaseBottomBarScreen() {
    val backStack = remember { mutableStateListOf<Any>(Routes.Create) }
    val listOfBottomRoutes = remember {
        listOf(Routes.Create, Routes.Files, Routes.Explore, Routes.Account)
    }
    val currentRoute = backStack.lastOrNull()

    val shouldShowBottomBar = currentRoute in listOfBottomRoutes
    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                val tabs = listOf(
                    SlippyTab(
                        name = stringResource(Res.string.tabOneCreate),
                        icon = painterResource(Res.drawable.createiconfinal),
                        selectedIcon = painterResource(Res.drawable.selectedcreate),
                        action = {
                            backStack.add(Routes.Create)
                        }
                    ),
                    SlippyTab(
                        name = stringResource(Res.string.tabTwoExplore),
                        icon = painterResource(Res.drawable.newexploreicon),
                        selectedIcon = painterResource(Res.drawable.selectedexploreicon),
                        action = {
                            backStack.add(Routes.Explore)
                        }
                    ),
                    SlippyTab(
                        name = "",
                        icon = painterResource(Res.drawable.createiconfinal),
                        action = {} // No action
                    ),
                    SlippyTab(
                        name = stringResource(Res.string.tabThreeFiles),
                        icon = painterResource(Res.drawable.iconfiles),
                        selectedIcon = painterResource(Res.drawable.selectedfile),
                        action = {
                            backStack.add(Routes.Files)
                        }
                    ),
                    SlippyTab(
                        name = stringResource(Res.string.tabFourProfile),
                        icon = painterResource(Res.drawable.profileiconnew),
                        selectedIcon = painterResource(Res.drawable.selectedprofile),
                        action = {
                            backStack.add(Routes.Account)
                        }
                    )
                )
                SlippyBottomBar(
                    bar = SlippyBar(
                        barStyle = SlippyBarStyle(backgroundColor = bottomBarBack),
                        textStyle = SlippyTextStyle(
                            enabledTextColor = selectedNavItem,
                            disabledTextColor = unselectedNavItem,
                            textSize = 12.sp
                        ),
                        iconStyle = SlippyIconStyle(
                            enabledIconColor = selectedNavItem,
                            disabledIconColor = unselectedNavItem
                        ),
                        startIndex = 0
                    ),
                    tabs = tabs,
                    iconSize = 24.dp,
                    fabIndex = 2
                )
            }
        },
        floatingActionButton = {
            if (shouldShowBottomBar) {
                FloatingActionButton(
                    onClick = {
                        // Your FAB action here
                    },
                    containerColor = Color(0xFF90EE90),
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 0.dp
                    ), modifier = Modifier.size(65.dp).offset(y = 54.dp), shape = CircleShape
                ) {
                    Image(
                        painterResource(Res.drawable.add_2_24px),
                        contentDescription = "Add",
                        colorFilter = ColorFilter.tint(color = Color.Black)
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        NavDisplay(
            modifier = Modifier.background(Color.White).padding(bottom = padding.calculateBottomPadding())
                .statusBarsPadding(),
            backStack = backStack,
            entryProvider = { key ->
                when (key) {
                    is Routes.Create -> NavEntry(key) {
                        CreateScreen()
                    }

                    is Routes.Explore -> NavEntry(key) {
                        ExploreScreen { }
                    }

                    is Routes.Files -> NavEntry(key) {
                        FilesScreen() {
                            backStack.add(Routes.FileEdit)
                        }
                    }

                    is Routes.FileEdit -> NavEntry(key) {
                        CreateEditScreen() {
                            backStack.removeLastOrNull()
                        }
                    }

                    is Routes.Account -> NavEntry(key) {
                        AccountScreen(onSubscriptionClick = {
                            backStack.add(Routes.Subscription)
                        }) {
                            backStack.add(Routes.Profile)
                        }
                    }

                    is Routes.Subscription -> NavEntry(key) {
                        SubscriptionScreen() {
                            backStack.removeLastOrNull()
                        }
                    }

                    is Routes.Profile -> NavEntry(key) {
                        ProfileScreen() {
                            backStack.removeLastOrNull()
                        }
                    }

                    else -> NavEntry(key) {
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                            Text("Nothing here!")
                        }
                    }
                }

            }
        )

    }
}