package com.example.aplikasi_mobile_mp_kp2.data.repository

import com.example.aplikasi_mobile_mp_kp2.data.remote.EmployeeInterface
import com.example.aplikasi_mobile_mp_kp2.data.remote.ManagerInterface
import okhttp3.MultipartBody

class EmployeeRepository(private val employeeInterface: EmployeeInterface) {

    suspend fun uploadBuktiTugas(idTugas: Int, file: MultipartBody.Part) =
        employeeInterface.uploadBuktiTugas(idTugas, file)

    suspend fun uploadFotoProfil(file: MultipartBody.Part) =
        employeeInterface.uploadFotoProfil(file)
}
