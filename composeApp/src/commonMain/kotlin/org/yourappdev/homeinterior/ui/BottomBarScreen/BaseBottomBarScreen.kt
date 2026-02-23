package org.yourappdev.homeinterior.ui.BottomBarScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import homeinterior.composeapp.generated.resources.*
import io.github.ismoy.imagepickerkmp.domain.models.GalleryPhotoResult
import io.github.ismoy.imagepickerkmp.presentation.ui.components.GalleryPickerLauncher
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.yourappdev.homeinterior.navigation.Routes
import org.yourappdev.homeinterior.ui.Account.AccountScreen
import org.yourappdev.homeinterior.ui.Account.ProfileScreen
import org.yourappdev.homeinterior.ui.Account.SubscriptionScreen
import org.yourappdev.homeinterior.ui.CreateAndExplore.Create.CreateScreen
import org.yourappdev.homeinterior.ui.CreateAndExplore.Explore.ExploreScreen
import org.yourappdev.homeinterior.ui.CreateAndExplore.RoomEvent
import org.yourappdev.homeinterior.ui.CreateAndExplore.RoomsViewModel
import org.yourappdev.homeinterior.ui.Files.CreateEditScreen
import org.yourappdev.homeinterior.ui.Files.FilesScreen
import org.yourappdev.homeinterior.ui.Generate.UiScreens.AboutToGenerateScreen
import org.yourappdev.homeinterior.ui.Generate.UiScreens.BaseGenerateScreen
import org.yourappdev.homeinterior.ui.Generate.UiScreens.ResultScreen
import org.yourappdev.homeinterior.ui.UiUtils.*
import org.yourappdev.homeinterior.ui.theme.bottomBarBack
import org.yourappdev.homeinterior.ui.theme.selectedNavItem
import org.yourappdev.homeinterior.ui.theme.unselectedNavItem

@Composable
fun BaseBottomBarScreen() {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val roomViewModel: RoomsViewModel =
        koinViewModel()
    var showGallery by remember { mutableStateOf(false) }

    val shouldShowBottomBar = currentDestination?.route?.let { route ->
        route.contains("Create") ||
                route.contains("Files") ||
                route.contains("Explore") ||
                route.contains("Account")
    } ?: false


    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                val tabs = listOf(
                    SlippyTab(
                        name = stringResource(Res.string.tabOneCreate),
                        icon = painterResource(Res.drawable.createiconfinal),
                        selectedIcon = painterResource(Res.drawable.selectedcreate),
                        action = {
                            navController.navigate(Routes.Create) {
                                popUpTo(Routes.Create) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    ),
                    SlippyTab(
                        name = stringResource(Res.string.tabTwoExplore),
                        icon = painterResource(Res.drawable.newexploreicon),
                        selectedIcon = painterResource(Res.drawable.selectedexploreicon),
                        action = {
                            navController.navigate(Routes.Explore) {
                                popUpTo(Routes.Create) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    ),
                    SlippyTab(
                        name = "",
                        icon = painterResource(Res.drawable.createiconfinal),
                        action = {
                            showGallery = true
                        } // Placeholder for FAB
                    ),
                    SlippyTab(
                        name = stringResource(Res.string.tabThreeFiles),
                        icon = painterResource(Res.drawable.files),
                        selectedIcon = painterResource(Res.drawable.selectedfile),
                        action = {
                            navController.navigate(Routes.Files) {
                                popUpTo(Routes.Create) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    ),
                    SlippyTab(
                        name = stringResource(Res.string.tabFourProfile),
                        icon = painterResource(Res.drawable.profileiconnew),
                        selectedIcon = painterResource(Res.drawable.selectedprofile),
                        action = {
                            navController.navigate(Routes.Account) {
                                popUpTo(Routes.Create) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
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
                        startIndex = getSelectedTabIndex(currentDestination.route)
                    ),
                    tabs = tabs,
                    iconSize = 24.dp,
                    fabIndex = 2
                )
            }
        },
        floatingActionButton = {
            if (shouldShowBottomBar) {
                Box(
                    modifier = Modifier
                        .offset(y = 50.dp)
                        .background(bottomBarBack, CircleShape)
                        .size(65.dp)
                ) {
                    FloatingActionButton(
                        onClick = {
                            showGallery = true
                        },
                        containerColor = Color(0xFFD4F7BD),
                        elevation = FloatingActionButtonDefaults.elevation(
                            defaultElevation = 0.dp
                        ),
                        modifier = Modifier
                            .size(60.dp)
                            .align(Alignment.Center),
                        shape = CircleShape
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .innerShadow(
                                    shape = CircleShape,
                                    shadow = Shadow(
                                        radius = 7.dp,
                                        color = Color.Black.copy(alpha = 0.2f),
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painterResource(Res.drawable.add),
                                contentDescription = "Add",
                                colorFilter = ColorFilter.tint(color = Color.Black),
                            )
                        }
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Create,
            modifier = Modifier
                .background(Color.White)
                .padding(bottom = padding.calculateBottomPadding())
                .statusBarsPadding()
        ) {
            // Bottom bar destinations
            composable<Routes.Create> {
                CreateScreen(
                    viewModel = roomViewModel,
                    onPremiumClick = {
                        navController.navigate(Routes.Subscription)
                    }
                )
            }

            composable<Routes.Explore> {
                ExploreScreen(
                    viewModel = roomViewModel
                )
            }

            composable<Routes.Files> {
                FilesScreen(
                    onImageClick = {
                        navController.navigate(Routes.FileEdit)
                    }
                )
            }

            composable<Routes.Account> {
                AccountScreen(
                    onSubscriptionClick = {
                        navController.navigate(Routes.Subscription)
                    },
                    onProfileClick = {
                        navController.navigate(Routes.Profile)
                    }
                )
            }

            composable<Routes.AddScreen> {
                BaseGenerateScreen(
                    roomViewModel,
                    endToNext = {
                        navController.navigate(Routes.AbtToGenerate)
                    },
                    onCloseClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable<Routes.FileEdit> {
                CreateEditScreen(
                    onClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable<Routes.AbtToGenerate> {
                AboutToGenerateScreen(
                    onCloseClick = {
                        navController.popBackStack()
                    },
                    onResult = {
                        navController.navigate(Routes.Result)
                    }
                )
            }

            composable<Routes.Subscription> {
                SubscriptionScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable<Routes.Result> {
                ResultScreen(
                    onCloseClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable<Routes.Profile> {
                ProfileScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
        if (showGallery) {
            GalleryPickerLauncher(
                onPhotosSelected = { photos ->
                    roomViewModel.onRoomEvent(RoomEvent.SetImage(imageDetails = photos.first()))
                    showGallery = false
                    navController.navigate(Routes.AddScreen)
                },
                onError = { showGallery = false },
                onDismiss = { showGallery = false },
                allowMultiple = false,
                selectionLimit = 1
            )
        }
    }
}

private fun getSelectedTabIndex(currentRoute: String?): Int {
    return when {
        currentRoute?.contains("Create") == true -> 0
        currentRoute?.contains("Explore") == true -> 1
        currentRoute?.contains("Files") == true -> 3
        currentRoute?.contains("Account") == true -> 4
        else -> 0
    }
}