package com.example.aplikasi_mobile_mp_kp2.screens.manager

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.aplikasi_mobile_mp_kp2.components.DrawerScaffoldLayout
import com.example.aplikasi_mobile_mp_kp2.navigation.Routes
import com.example.aplikasi_mobile_mp_kp2.screens.manager.home.ManagerHomeScreen

@Composable
fun ManagerScaffold(navController: NavHostController) {
    DrawerScaffoldLayout(
        navController = navController,
        drawerRoutes = listOf(
            Routes.ManagerHome,
            Routes.ManagerProjects,
            Routes.ManagerReport
        ),
        startDestination = Routes.ManagerHome.route,
        graphRoute = Routes.ManagerGraph.route
    ) {
        composable(Routes.ManagerHome.route) {
            ManagerHomeScreen()
        }
        composable(Routes.ManagerProjects.route) {
            Text("Proyek Manajer")
        }
        composable(Routes.ManagerReport.route) {
            Text("Laporan Manajer")
        }
    }
}
