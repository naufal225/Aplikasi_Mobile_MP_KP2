package com.example.aplikasi_mobile_mp_kp2.data.repository

import com.example.aplikasi_mobile_mp_kp2.data.model.AddProjectRequest
import com.example.aplikasi_mobile_mp_kp2.data.model.ProjectAddTaskRequest
import com.example.aplikasi_mobile_mp_kp2.data.model.ProjectUpdateTaskRequest
import com.example.aplikasi_mobile_mp_kp2.data.model.UpdateProjectRequest
import com.example.aplikasi_mobile_mp_kp2.data.model.UpdateStatusTaskAndProjectRequest
import com.example.aplikasi_mobile_mp_kp2.data.remote.ManagerInterface
import okhttp3.MultipartBody

class ManagerRepository(private val manajerInterface: ManagerInterface) {
    suspend fun getAllDataProyek() = manajerInterface.getAllDataProyek()

    suspend fun getDataProyekDone() = manajerInterface.getDataProyekDone()

    suspend fun getDataProyekProgress() = manajerInterface.getDataProyekInProgress()

    suspend fun getDataProyekById(id: Int) = manajerInterface.getDataProyekById(id)

    suspend fun addDataProyek(addProjectRequest: AddProjectRequest) = manajerInterface.addDataProyek(addProjectRequest)

    suspend fun getTugasByIdProyek(id: Int) = manajerInterface.getTugasByIdProyek(id)

    suspend fun updateDataProyek(id: Int, updateProjectRequest: UpdateProjectRequest) = manajerInterface.updateDataProyek(id, updateProjectRequest)

    suspend fun addTugasToProyek(id: Int, projectAddTaskRequest: ProjectAddTaskRequest) = manajerInterface.addTugasToProyek(id, projectAddTaskRequest)

    suspend fun getTugasByIdTugas(id: Int) = manajerInterface.getTugasByIdTugas(id)

    suspend fun getTugasByIdTugasWithBukti(id: Int) = manajerInterface.getTugasByIdTugasWithBukti(id)

    suspend fun updateDataTugas(id: Int, projectUpdateTaskRequest: ProjectUpdateTaskRequest) = manajerInterface.updateTugasByIdTugas(id, projectUpdateTaskRequest)

    suspend fun getAllKaryawanDivisi() = manajerInterface.getAllDataKaryawanInDivisi()

    suspend fun updateStatusTugas(id: Int, updateStatusTaskAndProjectRequest: UpdateStatusTaskAndProjectRequest) = manajerInterface.updateStatusTugas(id, updateStatusTaskAndProjectRequest)

    suspend fun updateStatusProyek(id: Int, updateStatusTaskAndProjectRequest: UpdateStatusTaskAndProjectRequest) = manajerInterface.updateStatusProyek(id, updateStatusTaskAndProjectRequest)

    suspend fun getDataUser() = manajerInterface.getDataUser()

    suspend fun uploadFotoProfil(file: MultipartBody.Part) =
        manajerInterface.uploadFotoProfil(file)
}