package com.example.bookshelf.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookshelf.screen.Detail.DetailScreen
import com.example.bookshelf.screen.home.HomeScreen
import com.example.bookshelf.screen.authentication.LogInScreen
import com.example.bookshelf.screen.search.SearchScreen
import com.example.bookshelf.screen.authentication.SignUpScreen
import com.example.bookshelf.screen.splash.SplashScreen
import com.example.bookshelf.screen.stat.StatScreen
import com.example.bookshelf.screen.update.UpdateScreen

@Composable
fun MyApp() {
    val navHostController = rememberNavController()

    NavHost(navController = navHostController, startDestination = Screen.SplashScreen.route) {

        composable(Screen.SplashScreen.route) { SplashScreen(navHostController) }
        composable(Screen.HomeScreen.route) { HomeScreen(navHostController) }
        composable(Screen.SearchScreen.route) { SearchScreen(navHostController) }
        composable(Screen.LogInScreen.route) { LogInScreen(navHostController) }
        composable(Screen.SignUpScreen.route) { SignUpScreen(navHostController) }
        composable(Screen.StatScreen.route) { StatScreen(navHostController) }
        composable(Screen.UpdateScreen.route+"/{id}") {
            val id = it.arguments?.getString("id")?:""
            UpdateScreen(navHostController,id) }

        composable(Screen.DetailScreen.route+"/{bookId}") {

            val id = it.arguments?.getString("bookId")

            if (id != null) {
                DetailScreen(navHostController, id = id )
            }


        }

    }
}