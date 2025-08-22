package com.example.github_user_search_app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.github_user_search_app.ui.screens.SearchScreen
import com.example.github_user_search_app.ui.screens.UserDetailScreen

sealed class Screen(val route: String) {
    data object Search : Screen("search")
    data object UserDetail : Screen("user_detail/{username}") {
        fun create(username: String) = "user_detail/$username"
    }
}

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.Search.route) {
        composable(Screen.Search.route) {
            SearchScreen(onUserClick = { username ->
                navController.navigate(Screen.UserDetail.create(username))
            })
        }
        composable(
            route = Screen.UserDetail.route,
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: return@composable
            UserDetailScreen(username = username, onBackClick = { navController.popBackStack() })
        }
    }
} 