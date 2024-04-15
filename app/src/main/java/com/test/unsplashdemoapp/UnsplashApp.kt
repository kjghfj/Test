package com.test.unsplashdemoapp

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navArgument
import com.test.unsplashdemoapp.theme.UnsplashTheme
import com.test.unsplashdemoapp.ui.SharedViewModel
import com.test.unsplashdemoapp.ui.detail.DetailView
import com.test.unsplashdemoapp.ui.favorites.FavoritesView
import com.test.unsplashdemoapp.ui.home.HomeView
import com.test.unsplashdemoapp.ui.screens.Screen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

val items = listOf(
    Screen.Images,
    Screen.Favorites,
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun UnsplashApp(sharedViewModel: SharedViewModel){
    UnsplashTheme {
        val scaffoldState = rememberScaffoldState()
        val navController = rememberAnimatedNavController()
        Scaffold(
            scaffoldState = scaffoldState,
            bottomBar = {
                if (currentRoute(navController) != Screen.Detail.route) {
                    BottomNavigation {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination

                        items.forEach { screen ->
                            BottomNavigationItem(
                                icon = {
                                    Icon(
                                        screen.icon ?: Icons.Filled.Dangerous,
                                        contentDescription = null
                                    )
                                },
                                label = { Text(stringResource(screen.resourceId)) },
                                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }

                                        launchSingleTop = true

                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            val actions = remember(navController) { MainActions(navController) }
            AnimatedNavHost(navController, startDestination = Screen.Images.route, Modifier.padding(innerPadding) ) {
                composable(Screen.Images.route) { HomeView(sharedViewModel, navController) }
                composable(Screen.Favorites.route) { FavoritesView(sharedViewModel, navController) }
                composable(
                    Screen.Detail.route,
                    arguments = listOf(navArgument("imageId") { type = NavType.StringType }),
                    enterTransition = { _, _ -> slideInVertically(initialOffsetY = { it / 2 }, animationSpec = tween(500)) },
                    exitTransition = { _, _ -> slideOutVertically(targetOffsetY = { it / 2 }, animationSpec = tween(500)) }
                ) { backStackEntry ->
                    DetailView(actions.upPress, backStackEntry.arguments?.getString("imageId")!!, sharedViewModel)
                }
            }
        }

    }
}

class MainActions(navController: NavHostController) {
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}