package com.example.aplikasi_mobile_mp_kp2.navigation

sealed class Routes(val route: String) {
    // Auth
    data object Login : Routes("login")
    data object AuthGraph : Routes("auth_graph")

    // Employee screens
    data object EmployeeGraph : Routes("employee_graph")
    data object EmployeeHome : Routes("employee_home")
    data object EmployeeTaskList : Routes("employee_tasks")
    data object EmployeeEvaluation : Routes("employee_evaluation")

    data object EmployeeGraphRoot : Routes("employee_graph_root")
    data object ManagerGraphRoot : Routes("manager_graph_root")

    // Manager screens
    data object ManagerGraph : Routes("manager_graph")
    data object ManagerHome : Routes("manager_home")
    data object ManagerProjects : Routes("manager_projects")
    data object ManagerReport : Routes("manager_report")
}
