package com.gallapillo.jobarecruit.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gallapillo.jobarecruit.common.Screen
import com.gallapillo.jobarecruit.presentation.items.BottomNavItem
import com.gallapillo.jobarecruit.presentation.items.BottomNavigationBar
import com.gallapillo.jobarecruit.presentation.screens.auth_screen.LoginScreen
import com.gallapillo.jobarecruit.presentation.screens.auth_screen.RegisterScreen
import com.gallapillo.jobarecruit.presentation.screens.auth_screen.AuthenticationViewModel
import com.gallapillo.jobarecruit.presentation.screens.profile.ProfileScreen
import com.gallapillo.jobarecruit.presentation.screens.profile.UserViewModel
import com.gallapillo.joba.presentation.theme.JobaTheme
import com.gallapillo.jobarecruit.presentation.screens.HelloScreen
import com.gallapillo.jobarecruit.presentation.screens.ResponsesScreen
import com.gallapillo.jobarecruit.presentation.screens.SearchVacancyScreen
import com.gallapillo.jobarecruit.presentation.screens.VacancyScreen
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JobaTheme {
                val navController = rememberNavController()
                var showBottomBar by rememberSaveable { mutableStateOf(true) }
                val navBackStackEntry by navController.currentBackStackEntryAsState()

                val authenticationViewModel: AuthenticationViewModel = hiltViewModel()
                val userViewModel: UserViewModel = hiltViewModel()

                showBottomBar = when (navBackStackEntry?.destination?.route) {
                    Screen.HelloScreen.route -> false
                    Screen.RegisterScreen.route -> false
                    Screen.LoginScreen.route -> false
                    else -> true
                }

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            BottomNavigationBar(
                                items = listOf(
                                    BottomNavItem(
                                        name = "Вакансии",
                                        route = Screen.MainScreen.route,
                                        icon = Icons.Default.Home
                                    ),
                                    BottomNavItem(
                                        name = "Поиск вакансий",
                                        route = Screen.SearchScreen.route,
                                        icon = Icons.Default.Search
                                    ),
                                    BottomNavItem(
                                        name = "Отклики",
                                        route = Screen.ResponsesScreen.route,
                                        icon = Icons.Default.Notifications
                                    ),
                                    BottomNavItem(
                                        name = "Мой профиль",
                                        route = Screen.ProfileScreen.route,
                                        icon = Icons.Default.Person
                                    )
                                ),
                                navController = navController,
                                onItemClick = {
                                    navController.navigate(it.route) {
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
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.HelloScreen.route
                    ) {
                        composable(route = Screen.HelloScreen.route) {
                            HelloScreen(navController, authenticationViewModel)
                        }
                        composable(route = Screen.LoginScreen.route) {
                            LoginScreen(navController, authenticationViewModel)
                        }
                        composable(route = Screen.RegisterScreen.route) {
                            RegisterScreen(navController, authenticationViewModel)
                        }
                        composable(route = Screen.MainScreen.route) {
                            VacancyScreen(navController, userViewModel, authenticationViewModel)
                        }
                        composable(route = Screen.SearchScreen.route) {
                            SearchVacancyScreen(navController = navController)
                        }
                        composable(route = Screen.ResponsesScreen.route) {
                            ResponsesScreen(navController = navController)
                        }
                        composable(route = Screen.ProfileScreen.route) {
                            ProfileScreen(navController = navController, userViewModel, authenticationViewModel)
                        }
                    }
                }
            }
        }
    }
}

