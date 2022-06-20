package com.gallapillo.jobarecruit.common

sealed class Screen(val route: String) {
    object HelloScreen : Screen("hello_screen")
    object LoginScreen : Screen("login_screen")
    object RegisterScreen : Screen("register_screen")
    object MainScreen : Screen("main_screen")
    object SearchScreen : Screen("search_screen")
    object ResponsesScreen : Screen("responses_screen")
    object ProfileScreen : Screen("profile_screen")
}
