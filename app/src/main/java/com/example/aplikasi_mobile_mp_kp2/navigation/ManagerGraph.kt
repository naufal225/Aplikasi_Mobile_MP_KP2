package com.example.aplikasi_mobile_mp_kp2.navigation

import androidx.compose.material3.Scaffold
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.aplikasi_mobile_mp_kp2.screens.manager.ManagerScaffold
import com.example.aplikasi_mobile_mp_kp2.screens.manager.home.ManagerHomeScreen

fun NavGraphBuilder.managerGraph(navController: NavHostController) {
    composable(Routes.ManagerGraphRoot.route) {
        ManagerScaffold(navController)
    }
}
