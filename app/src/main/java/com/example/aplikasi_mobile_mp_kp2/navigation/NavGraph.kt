package com.example.aplikasi_mobile_mp_kp2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.aplikasi_mobile_mp_kp2.viewmodel.AuthViewModel
import com.example.aplikasi_mobile_mp_kp2.viewmodel.manager.ManagerViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Routes.AuthGraph.route,
    authViewModel: AuthViewModel,
    managerViewModel: ManagerViewModel
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authGraph(navController, authViewModel)
        managerGraph(navController, managerViewModel)
        employeeGraph(navController)
    }
}
