package com.example.aplikasi_mobile_mp_kp2.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.aplikasi_mobile_mp_kp2.screens.login.LoginScreen
import com.example.aplikasi_mobile_mp_kp2.viewmodel.AuthViewModel

fun NavGraphBuilder.authGraph(navController: NavHostController, authViewModel: AuthViewModel) {
    navigation(
        startDestination = Routes.Login.route,
        route = Routes.AuthGraph.route
    ) {
        composable(Routes.Login.route) {
            LoginScreen(navController,authViewModel)
        }
    }
}
