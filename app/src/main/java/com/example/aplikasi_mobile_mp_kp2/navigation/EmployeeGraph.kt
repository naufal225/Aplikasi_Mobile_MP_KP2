package com.example.aplikasi_mobile_mp_kp2.navigation

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.aplikasi_mobile_mp_kp2.components.DrawerScaffoldLayout
import com.example.aplikasi_mobile_mp_kp2.screens.employee.home.EmployeeHomeScreen
import com.example.aplikasi_mobile_mp_kp2.screens.employee.project.EmployeeProjectAddBuktiTugas
import com.example.aplikasi_mobile_mp_kp2.screens.employee.project.EmployeeProjectDetailScreen
import com.example.aplikasi_mobile_mp_kp2.screens.employee.project.EmployeeProjectScreen
import com.example.aplikasi_mobile_mp_kp2.screens.manager.profile.EmployeeProfileScreen
import com.example.aplikasi_mobile_mp_kp2.screens.manager.profile.ManagerProfileScreen
import com.example.aplikasi_mobile_mp_kp2.viewmodel.AuthViewModel
import com.example.aplikasi_mobile_mp_kp2.viewmodel.employee.EmployeeViewModel
import com.example.aplikasi_mobile_mp_kp2.viewmodel.manager.ManagerViewModel

fun NavGraphBuilder.employeeGraph(navController: NavHostController, managerViewModel: ManagerViewModel, employeeViewModel: EmployeeViewModel, authViewModel: AuthViewModel) {
    navigation(
        startDestination = Routes.EmployeeHome.route,
        route = Routes.EmployeeGraph.route
    ) {
        val routeDrawer = listOf(
            Routes.EmployeeHome,
            Routes.EmployeeProjects,
            Routes.EmployeeProfile
        )

        composable(Routes.EmployeeHome.route) {
            DrawerScaffoldLayout(
                drawerRoutes = routeDrawer,
                navController = navController
            ) {
                EmployeeHomeScreen(navController, it, employeeViewModel)
            }
        }

        composable(Routes.EmployeeProjects.route) {
            DrawerScaffoldLayout(
                drawerRoutes = routeDrawer,
                navController = navController
            ) {
                EmployeeProjectScreen(managerViewModel, navController, it)
            }
        }

        composable(
            route = "employee_project_detail/{projectId}",
            arguments = listOf(navArgument("projectId") { type = NavType.StringType })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId") ?: ""
            DrawerScaffoldLayout(
                drawerRoutes = routeDrawer,
                navController = navController
            ) {
                EmployeeProjectDetailScreen(projectId, it, managerViewModel, navController)
            }
        }

        composable(
            route = "employee_project_add_bukti_tugas/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.StringType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId") ?: ""
            DrawerScaffoldLayout(
                drawerRoutes = routeDrawer,
                navController = navController
            ) {
                EmployeeProjectAddBuktiTugas(it, employeeViewModel, taskId.toInt(), navController)
            }
        }

        composable(Routes.EmployeeTaskList.route) {
            DrawerScaffoldLayout(
                drawerRoutes = routeDrawer,
                navController = navController
            ) {
                Text("Tugas Karyawan")
            }
        }

        composable(Routes.EmployeeEvaluation.route) {
            DrawerScaffoldLayout(
                drawerRoutes = routeDrawer,
                navController = navController
            ) {
                Text("Evaluasi Karyawan")
            }
        }

        composable(
            route = Routes.EmployeeProfile.route
        ) {
            DrawerScaffoldLayout(
                drawerRoutes = routeDrawer,
                navController = navController
            ) {

                EmployeeProfileScreen(navController, modifier = it, employeeViewModel, authViewModel)
            }
        }
    }
}

