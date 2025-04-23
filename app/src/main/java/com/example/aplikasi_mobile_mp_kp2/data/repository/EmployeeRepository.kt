package com.example.aplikasi_mobile_mp_kp2.data.repository

import com.example.aplikasi_mobile_mp_kp2.data.model.NotificationRequest
import com.example.aplikasi_mobile_mp_kp2.data.remote.EmployeeInterface
import com.example.aplikasi_mobile_mp_kp2.data.remote.ManagerInterface
import okhttp3.MultipartBody

class EmployeeRepository(private val employeeInterface: EmployeeInterface) {

    suspend fun uploadBuktiTugas(idTugas: Int, files: List<MultipartBody.Part>) =
        employeeInterface.uploadBuktiTugas(idTugas, files)

    suspend fun uploadFotoProfil(file: MultipartBody.Part) =
        employeeInterface.uploadFotoProfil(file)

    suspend fun getDataUser() = employeeInterface.getDataUser()

    suspend fun createNotification(notificationRequest: NotificationRequest) = employeeInterface.createNotification(notificationRequest)

    suspend fun getAllDataNotification() = employeeInterface.getAllDataNotification()
}
