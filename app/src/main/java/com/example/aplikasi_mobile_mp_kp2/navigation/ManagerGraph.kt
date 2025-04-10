package com.example.aplikasi_mobile_mp_kp2.navigation

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.aplikasi_mobile_mp_kp2.components.DrawerScaffoldLayout
import com.example.aplikasi_mobile_mp_kp2.screens.manager.home.ManagerHomeScreen
import com.example.aplikasi_mobile_mp_kp2.screens.manager.profile.ManagerProfileScreen
import com.example.aplikasi_mobile_mp_kp2.screens.manager.project.ManagerProjectAddScreen
import com.example.aplikasi_mobile_mp_kp2.screens.manager.project.ManagerProjectAddTaskScreen
import com.example.aplikasi_mobile_mp_kp2.screens.manager.project.ManagerProjectBuktiTaskScreen
import com.example.aplikasi_mobile_mp_kp2.screens.manager.project.ManagerProjectDetailScreen
import com.example.aplikasi_mobile_mp_kp2.screens.manager.project.ManagerProjectUpdateScreen
import com.example.aplikasi_mobile_mp_kp2.screens.manager.project.ManagerProjectUpdateTaskScreen
import com.example.aplikasi_mobile_mp_kp2.screens.manager.project.ManagerProyekScreen
import com.example.aplikasi_mobile_mp_kp2.viewmodel.manager.ManagerViewModel

fun NavGraphBuilder.managerGraph(
    navController: NavHostController,
    managerViewModel: ManagerViewModel
) {
    val drawerRoute = listOf(Routes.ManagerHome, Routes.ManagerProjects, Routes.ManagerProfile)
    navigation(
        startDestination = Routes.ManagerHome.route,
        route = Routes.ManagerGraph.route
    ) {
        // Manager
        composable(Routes.ManagerHome.route) {
            DrawerScaffoldLayout(
                drawerRoutes = drawerRoute,
                navController = navController
            ) {
            ManagerHomeScreen(managerViewModel, it)
            }
        }

        composable(Routes.ManagerProjects.route) {
            DrawerScaffoldLayout(
                drawerRoutes = drawerRoute,
                navController = navController
            ) {
                ManagerProyekScreen(managerViewModel, navController, it)
            }
        }

        composable(
            route = "manager_project_detail/{projectId}",
            arguments = listOf(navArgument("projectId") { type = NavType.StringType })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId") ?: ""
            DrawerScaffoldLayout(
                drawerRoutes = drawerRoute,
                navController = navController
            ) {
                ManagerProjectDetailScreen(projectId, it, managerViewModel, navController)
            }
        }

        composable(
            route = Routes.ManagerProjectAdd.route,
        ) {
            DrawerScaffoldLayout(
                drawerRoutes = drawerRoute,
                navController = navController
            ) {
                ManagerProjectAddScreen(managerViewModel, navController, modifier = it)
            }
        }

        composable(
            route = "manager_project_update/{projectId}",
            arguments = listOf(navArgument("projectId") { type = NavType.StringType })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId") ?: ""
            DrawerScaffoldLayout(
                drawerRoutes = drawerRoute,
                navController = navController
            ) {
                ManagerProjectUpdateScreen(modifier = it, managerViewModel = managerViewModel, id = projectId, navController = navController)
            }
        }

        composable(
            route = "manager_add_task/{projectId}",
            arguments = listOf(navArgument("projectId") { type = NavType.StringType })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId") ?: ""
            DrawerScaffoldLayout(
                drawerRoutes = drawerRoute,
                navController = navController
            ) {
                ManagerProjectAddTaskScreen(modifier = it, managerViewModel = managerViewModel, projectId = projectId.toInt(), navController = navController)
            }
        }

        composable(
            route = "manager_update_task/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.StringType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId") ?: ""
            DrawerScaffoldLayout(
                drawerRoutes = drawerRoute,
                navController = navController
            ) {
                ManagerProjectUpdateTaskScreen(modifier = it, managerViewModel = managerViewModel, taskId=taskId.toInt(), navController = navController)
            }
        }

        composable(
            route = "manager_bukti_task/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.StringType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId") ?: ""
            DrawerScaffoldLayout(
                drawerRoutes = drawerRoute,
                navController = navController
            ) {
                ManagerProjectBuktiTaskScreen(modifier = it, managerViewModel = managerViewModel, taskId=taskId, navController = navController)
            }
        }

        composable(
            route = Routes.ManagerProfile.route
        ) {
            DrawerScaffoldLayout(
                drawerRoutes = drawerRoute,
                navController = navController
            ) {

                ManagerProfileScreen(modifier = it, managerViewModel)
            }
        }

        composable(Routes.ManagerReport.route) {
            DrawerScaffoldLayout(
                drawerRoutes = drawerRoute,
                navController = navController
            ) {
                Text("Laporan Manajer")
            }
        }


    }
}
