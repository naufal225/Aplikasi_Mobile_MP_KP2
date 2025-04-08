package com.example.aplikasi_mobile_mp_kp2.data.remote

import com.example.aplikasi_mobile_mp_kp2.data.model.AddProjectRequest
import com.example.aplikasi_mobile_mp_kp2.data.model.AddProjectResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.AllKaryawanDivisiResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.DataTugasByIdProyekResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.ProjectAddTaskRequest
import com.example.aplikasi_mobile_mp_kp2.data.model.ProjectAddTaskResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.ProjectUpdateTaskRequest
import com.example.aplikasi_mobile_mp_kp2.data.model.ProjectUpdateTaskResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.ProyekByIdResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.ProyekProgressResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.TugasByIdTugasResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.TugasByIdTugasWithBuktiResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.UpdateProjectRequest
import com.example.aplikasi_mobile_mp_kp2.data.model.UpdateProjectResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.UpdateStatusProyekResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.UpdateStatusTaskAndProjectRequest
import com.example.aplikasi_mobile_mp_kp2.data.model.UpdateStatusTugasResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ManagerInterface {
    @GET("manajer/proyek-all")
    suspend fun getAllDataProyek() : Response<ProyekProgressResponse>

    @GET("manajer/get-all-karyawan-by-divisi")
    suspend fun getAllDataKaryawanInDivisi() : Response<AllKaryawanDivisiResponse>

    @GET("manajer/proyek-done")
    suspend fun getDataProyekDone() : Response<ProyekProgressResponse>

    @GET("manajer/proyek-in-progress")
    suspend fun getDataProyekInProgress() : Response<ProyekProgressResponse>

    @GET("manajer/proyek-by-id/{id}")
    suspend fun getDataProyekById(@Path("id") id: Int) : Response<ProyekByIdResponse>

    @POST("manajer/proyek-add")
    suspend fun addDataProyek(@Body addProjectRequest: AddProjectRequest) : Response<AddProjectResponse>

    @GET("manajer/get-tugas-by-id-proyek/{id}")
    suspend fun getTugasByIdProyek(@Path("id") id: Int) : Response<DataTugasByIdProyekResponse>

    @GET("manajer/get-tugas-by-id-tugas/{id}")
    suspend fun getTugasByIdTugas(@Path("id") id: Int) : Response<TugasByIdTugasResponse>

    @GET("manajer/get-tugas-by-id-tugas-with-bukti/{id}")
    suspend fun getTugasByIdTugasWithBukti(@Path("id") id: Int) : Response<TugasByIdTugasWithBuktiResponse>


    @PUT("manajer/update-proyek/{id}")
    suspend fun updateDataProyek(@Path("id") id: Int, @Body updateProjectRequest: UpdateProjectRequest) : Response<UpdateProjectResponse>

    @POST("manajer/add-tugas/{id}")
    suspend fun addTugasToProyek(@Path("id") id: Int, @Body projectAddTaskRequest: ProjectAddTaskRequest) : Response<ProjectAddTaskResponse>

    @PUT("manajer/update-tugas/{id}")
    suspend fun updateTugasByIdTugas(@Path("id") id: Int, @Body projectUpdateTaskRequest: ProjectUpdateTaskRequest) : Response<ProjectUpdateTaskResponse>

    @PUT("manajer/update-status-tugas/{id}")
    suspend fun updateStatusTugas(@Path("id") id: Int, @Body updateStatusTaskAndProjectRequest: UpdateStatusTaskAndProjectRequest): Response<UpdateStatusTugasResponse>

    @PUT("manajer/update-status-proyek/{id}")
    suspend fun updateStatusProyek(@Path("id") id: Int, @Body updateStatusTaskAndProjectRequest: UpdateStatusTaskAndProjectRequest) : Response<UpdateStatusProyekResponse>
}