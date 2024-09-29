package com.example.bookshelf.navigation

sealed class Screen(val route: String) {
    data object DetailScreen : Screen(route = "detailScreen")
    data object HomeScreen : Screen(route = "homeScreen")
    data object LogInScreen : Screen(route = "loginScreen")
    data object SearchScreen : Screen(route = "searchScreen")
    data object SplashScreen : Screen(route = "splashScreen")
    data object StatScreen : Screen(route = "statScreen")
    data object UpdateScreen : Screen(route = "updateScreen")
    data object SignUpScreen : Screen(route = "signupScreen")
}