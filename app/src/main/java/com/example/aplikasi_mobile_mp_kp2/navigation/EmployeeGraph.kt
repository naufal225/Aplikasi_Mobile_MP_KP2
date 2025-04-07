package com.example.aplikasi_mobile_mp_kp2.navigation

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.aplikasi_mobile_mp_kp2.components.DrawerScaffoldLayout
import com.example.aplikasi_mobile_mp_kp2.screens.employee.home.EmployeeHomeScreen

fun NavGraphBuilder.employeeGraph(navController: NavHostController) {
    navigation(
        startDestination = Routes.EmployeeHome.route,
        route = Routes.EmployeeGraph.route
    ) {
        composable(Routes.EmployeeHome.route) {
            DrawerScaffoldLayout(
                drawerRoutes = listOf(
                    Routes.EmployeeHome,
                    Routes.EmployeeTaskList,
                    Routes.EmployeeEvaluation
                ),
                navController = navController
            ) {
                EmployeeHomeScreen(it)
            }
        }

        composable(Routes.EmployeeTaskList.route) {
            DrawerScaffoldLayout(
                drawerRoutes = listOf(
                    Routes.EmployeeHome,
                    Routes.EmployeeTaskList,
                    Routes.EmployeeEvaluation
                ),
                navController = navController
            ) {
                Text("Tugas Karyawan")
            }
        }

        composable(Routes.EmployeeEvaluation.route) {
            DrawerScaffoldLayout(
                drawerRoutes = listOf(
                    Routes.EmployeeHome,
                    Routes.EmployeeTaskList,
                    Routes.EmployeeEvaluation
                ),
                navController = navController
            ) {
                Text("Evaluasi Karyawan")
            }
        }
    }
}

