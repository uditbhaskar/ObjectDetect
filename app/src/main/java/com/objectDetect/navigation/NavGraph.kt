package com.objectDetect.navigation

import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.objectDetect.presentation.ui.composeScreen.OnboardingScreenRoot
import com.objectDetect.presentation.ui.composeScreen.ResultScreen
import com.objectDetect.util.AppConstants

/**
 * Sets up the app's navigation graph with onboarding and home destinations.
 * @author udit
 */
@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = AppConstants.NAV_ONBOARD) {
        composable(AppConstants.NAV_ONBOARD) {
            OnboardingScreenRoot(
                onNavigateNext = { uriString ->
                    navController.navigate("${AppConstants.NAV_RESULT}/$uriString")
                }
            )
        }
        composable(
            route = "${AppConstants.NAV_RESULT}/{imageUri}",
            arguments = listOf(navArgument("imageUri") { type = NavType.StringType })
        ) { backStackEntry ->
            val imageUri = backStackEntry.arguments?.getString("imageUri")
            ResultScreen(
                imageUri = imageUri,
                onBackToMain = { navController.popBackStack(AppConstants.NAV_ONBOARD, inclusive = false) }
            )
        }
    }
}