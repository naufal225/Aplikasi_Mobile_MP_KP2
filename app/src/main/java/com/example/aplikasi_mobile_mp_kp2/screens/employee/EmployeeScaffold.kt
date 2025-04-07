package com.example.aplikasi_mobile_mp_kp2.screens.employee

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.aplikasi_mobile_mp_kp2.components.DrawerScaffoldLayout
import com.example.aplikasi_mobile_mp_kp2.navigation.Routes
import com.example.aplikasi_mobile_mp_kp2.screens.employee.home.EmployeeHomeScreen

@Composable
fun EmployeeScaffold(navController: NavHostController) {
//    DrawerScaffoldLayout(
//        drawerRoutes = listOf(
//            Routes.EmployeeHome,
//            Routes.EmployeeTaskList,
//            Routes.EmployeeEvaluation
//        ),
//        startDestination = Routes.EmployeeHome.route,
//        graphRoute = Routes.EmployeeGraph.route
//    ) {
//        composable(Routes.EmployeeHome.route) {
//            EmployeeHomeScreen()
//        }
//        composable(Routes.EmployeeTaskList.route) {
//            Text("Tugas Karyawan")
//        }
//        composable(Routes.EmployeeEvaluation.route) {
//            Text("Evaluasi Karyawan")
//        }
//    }
}
