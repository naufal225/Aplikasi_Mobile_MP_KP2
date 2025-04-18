package com.example.aplikasi_mobile_mp_kp2.navigation

import android.graphics.drawable.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Task
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.aplikasi_mobile_mp_kp2.data.model.KaryawanX
import com.example.aplikasi_mobile_mp_kp2.data.model.Proyek
import okhttp3.Route

sealed class Routes(val route: String, val label: String? = null, val icon: ImageVector? = null) {
    data object Login : Routes("login")

    // Employee
    data object EmployeeHome : Routes("employee_home", "Beranda", Icons.Default.Home)

    data object EmployeeProjects : Routes("employee_projects", "Projek", Icons.Default.Task)

    data object EmployeeProjectDetail : Routes("employee_project_detail" + "/{projectId}", "Detail Projek") {
        fun employeeProjectDetailWithId(projectId: String): String {
            return "employee_project_detail/$projectId"
        }
    }

    data object EmployeeProjectAddBuktiTugas : Routes("employee_project_add_bukti_tugas" + "/{taskId}", "Upload Bukti") {
        fun employeeProjectAddBuktiTugas(taskId: String) : String {
            return "employee_project_add_bukti_tugas/${taskId}"
        }
    }

    data object EmployeeProfile : Routes("employee_profile_screen","Profil", Icons.Default.Person)


    data object EmployeeTaskList : Routes("employee_tasks", "Tugas")
    data object EmployeeEvaluation : Routes("employee_evaluation", "Evaluasi")



    // Manager
    data object ManagerHome : Routes("manager_home", "Beranda", Icons.Default.Home)
    data object ManagerProjects : Routes("manager_projects", "Projek", Icons.Default.Task)

    data object ManagerProjectDetail : Routes("manager_project_detail" + "/{projectId}", "Detail Projek") {
        fun managerProjectDetailWithId(projectId: String): String {
            return "manager_project_detail/$projectId"
        }
    }

    data object ManagerProjectAdd : Routes("manager_project_add", "Tambah Proyek")

    data object ManagerProjectUpdate : Routes("manajer_project_update" + "/{projectId}" , "Perbarui Data Proyek") {
        fun managerProjectUpdate(projectId: String): String {
            return "manager_project_update/$projectId"
        }
    }

    data object ManagerAddTask : Routes("manager_add_task" + "/{projectId}", "Tambah Data Tugas") {
        fun addTaskWithIdProject(projectId: String): String {
            return "manager_add_task/$projectId"
        }
    }

    data object ManagerProjectUpdateTask : Routes("manager_update_task" + "/{taskId}" , "Perbarui Data Tugas") {
        fun managerProjectUpdateTask(taskId: String): String {
            return "manager_update_task/$taskId"
        }
    }

    data object ManagerProjectBuktiTask : Routes("manager_bukti_task" + "/{taskId}" , "Bukti Tugas Selesai") {
        fun managerProjectBuktiTask(taskId: String): String {
            return "manager_bukti_task/$taskId"
        }
    }

    data object ManagerProfile : Routes("manager_profile_screen","Profil", Icons.Default.Person)

    data object ManagerReport : Routes("manager_report", "Laporan")

    // Nested graph wrapper
    data object AuthGraph : Routes("auth")
    data object EmployeeGraph : Routes("employee_graph_root")
    data object ManagerGraph : Routes("manager_graph_root")
}



