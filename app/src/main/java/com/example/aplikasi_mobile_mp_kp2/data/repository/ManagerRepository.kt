package com.example.aplikasi_mobile_mp_kp2.data.repository

import com.example.aplikasi_mobile_mp_kp2.data.model.AddProjectRequest
import com.example.aplikasi_mobile_mp_kp2.data.model.UpdateProjectRequest
import com.example.aplikasi_mobile_mp_kp2.data.remote.ManagerInterface

class ManagerRepository(private val manajerInterface: ManagerInterface) {
    suspend fun getAllDataProyek() = manajerInterface.getAllDataProyek()

    suspend fun getDataProyekDone() = manajerInterface.getDataProyekDone()

    suspend fun getDataProyekProgress() = manajerInterface.getDataProyekInProgress()

    suspend fun getDataProyekById(id: Int) = manajerInterface.getDataProyekById(id)

    suspend fun addDataProyek(addProjectRequest: AddProjectRequest) = manajerInterface.addDataProyek(addProjectRequest)

    suspend fun getTugasByIdProyek(id: Int) = manajerInterface.getTugasByIdProyek(id)

    suspend fun updateDataProyek(id: Int, updateProjectRequest: UpdateProjectRequest) = manajerInterface.updateDataProyek(id, updateProjectRequest)
}