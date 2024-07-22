package com.example.mockapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mockapp.ui.screen.HomeDestination
import com.example.mockapp.ui.screen.HomeScreenRoot
import com.example.mockapp.ui.screen.detail.DetailDestination
import com.example.mockapp.ui.screen.detail.DetailScreenRoot

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val popBackstack: () -> Unit = {
        navController.popBackStack()
    }

    val navigateUp: () -> Unit = {
        navController.navigateUp()
    }

    val navigateTo: (String) -> Unit = { route ->
        navController.navigate(route)
    }

    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreenRoot(
                navigateToDetail = {
                    navigateTo("${DetailDestination.route}/$it")
                }
            )
        }
        composable(
            route = DetailDestination.routeWithArgs,
            arguments = listOf(
                navArgument(DetailDestination.pokemonName) {
                    type = NavType.StringType
                }
            )
        ) {
            DetailScreenRoot(
                navigateUp = navigateUp
            )
        }
    }
}