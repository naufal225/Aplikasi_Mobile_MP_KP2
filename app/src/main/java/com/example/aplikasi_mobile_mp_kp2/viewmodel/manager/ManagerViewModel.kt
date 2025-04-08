package com.example.aplikasi_mobile_mp_kp2.viewmodel.manager

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.aplikasi_mobile_mp_kp2.data.model.AddProjectRequest
import com.example.aplikasi_mobile_mp_kp2.data.model.AddProjectResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.AllKaryawanDivisiResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.DataTugasByIdProyekResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.ProjectAddTaskRequest
import com.example.aplikasi_mobile_mp_kp2.data.model.ProjectAddTaskResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.ProjectUpdateTaskRequest
import com.example.aplikasi_mobile_mp_kp2.data.model.ProjectUpdateTaskResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.Proyek
import com.example.aplikasi_mobile_mp_kp2.data.model.ProyekByIdResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.ProyekProgressResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.TugasByIdTugasResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.TugasByIdTugasWithBuktiResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.UpdateProjectRequest
import com.example.aplikasi_mobile_mp_kp2.data.model.UpdateProjectResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.UpdateStatusProyekResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.UpdateStatusTaskAndProjectRequest
import com.example.aplikasi_mobile_mp_kp2.data.model.UpdateStatusTugasResponse
import com.example.aplikasi_mobile_mp_kp2.data.remote.NetworkResponse
import com.example.aplikasi_mobile_mp_kp2.data.remote.RetrofitInstance
import com.example.aplikasi_mobile_mp_kp2.data.repository.ManagerRepository
import kotlinx.coroutines.launch

class ManagerViewModel(application: Application) : AndroidViewModel(application) {

    private val managerApi = RetrofitInstance.getInstanceManagerInterface(application.applicationContext)
    private val managerRepository = ManagerRepository(managerApi)

    private val _proyek_done = MutableLiveData<NetworkResponse<List<Proyek>>>()
    private val _proyek_in_progress = MutableLiveData<NetworkResponse<List<Proyek>>>()
    private val _jumlah_proyek_done = MutableLiveData<NetworkResponse<Int>>()
    private val _jumlah_proyek_in_progress = MutableLiveData<NetworkResponse<Int>>()

    private val _response_all_proyek = MutableLiveData<NetworkResponse<ProyekProgressResponse>>()
    private val _response_proyek_done = MutableLiveData<NetworkResponse<ProyekProgressResponse>>()
    private val _response_proyek_in_progress = MutableLiveData<NetworkResponse<ProyekProgressResponse>>()

    private val _response_by_id_proyek = MutableLiveData<NetworkResponse<ProyekByIdResponse>>()

    private val _response_add_project = MutableLiveData<NetworkResponse<AddProjectResponse>>()

    private val _response_tugas_by_id_project = MutableLiveData<NetworkResponse<DataTugasByIdProyekResponse>>()

    private val _response_tugas_by_id_tugas = MutableLiveData<NetworkResponse<TugasByIdTugasResponse>>()

    private val _response_tugas_by_id_tugas_with_bukti = MutableLiveData<NetworkResponse<TugasByIdTugasWithBuktiResponse>>()

    private val _response_update_project = MutableLiveData<NetworkResponse<UpdateProjectResponse>>()

    private val _response_add_tugas = MutableLiveData<NetworkResponse<ProjectAddTaskResponse>>()

    private val _response_karyawan_in_divisi = MutableLiveData<NetworkResponse<AllKaryawanDivisiResponse>>()

    private val _response_update_tugas = MutableLiveData<NetworkResponse<ProjectUpdateTaskResponse>>()

    private val _response_update_status_tugas = MutableLiveData<NetworkResponse<UpdateStatusTugasResponse>>()

    private val _response_update_status_proyek = MutableLiveData<NetworkResponse<UpdateStatusProyekResponse>>()

    val proyek_done = _proyek_done
    val proyek_in_progress = _proyek_in_progress
    val jumlah_proyek_done = _jumlah_proyek_done
    val jumlah_proyek_in_progress = _jumlah_proyek_in_progress

    val response_by_id_proyek = _response_by_id_proyek

    val response_all_proyek = _response_all_proyek
    val response_proyek_done = _response_proyek_done
    val response_proyek_in_progress = _response_proyek_in_progress

    val response_add_project = _response_add_project

    val response_tugas_by_id_project = _response_tugas_by_id_project

    val response_update_project = _response_update_project

    val response_add_tugas = _response_add_tugas

    val response_karyawan_in_divisi = _response_karyawan_in_divisi

    val response_update_tugas = _response_update_tugas

    val response_tugas_by_id_tugas = _response_tugas_by_id_tugas

    val response_update_status_tugas = _response_update_status_tugas

    val response_update_status_proyek = _response_update_status_proyek

    val response_tugas_by_id_tugas_with_bukti = _response_tugas_by_id_tugas_with_bukti

    fun getDataAllProyek() {
        _proyek_done.postValue(NetworkResponse.LOADING)
        _proyek_in_progress.postValue(NetworkResponse.LOADING)
        _jumlah_proyek_done.postValue(NetworkResponse.LOADING)
        _jumlah_proyek_in_progress.postValue(NetworkResponse.LOADING)
        _response_all_proyek.postValue(NetworkResponse.LOADING)

        viewModelScope.launch {
            try {
                val responseDone = managerRepository.getDataProyekDone()
                val responseProgress = managerRepository.getDataProyekProgress()
                val responseAllProyek = managerRepository.getAllDataProyek()

                if (responseAllProyek.isSuccessful && responseAllProyek.body() != null) {
                    _response_all_proyek.postValue(NetworkResponse.SUCCESS(responseAllProyek.body()!!))

                } else {
                    _response_all_proyek.postValue(NetworkResponse.ERROR("Gagal memuat proyek selesai"))
                }

                if (responseDone.isSuccessful && responseDone.body() != null) {
                    val proyekList = responseDone.body()!!.data.proyek
                    val count = responseDone.body()!!.data.jumlah
                    _proyek_done.postValue(NetworkResponse.SUCCESS(proyekList))
                    _jumlah_proyek_done.postValue(NetworkResponse.SUCCESS(count))

                    _response_proyek_done.postValue(NetworkResponse.SUCCESS(responseDone.body()!!))

                } else {
                    _proyek_done.postValue(NetworkResponse.ERROR("Gagal memuat proyek selesai"))
                    _jumlah_proyek_done.postValue(NetworkResponse.ERROR("Gagal memuat jumlah proyek selesai"))
                    _response_proyek_done.postValue(NetworkResponse.ERROR("Gagal memuat jumlah proyek selesai"))

                }

                if (responseProgress.isSuccessful && responseProgress.body() != null) {
                    val proyekList = responseProgress.body()!!.data.proyek
                    val count = responseProgress.body()!!.data.jumlah
                    _proyek_in_progress.postValue(NetworkResponse.SUCCESS(proyekList))
                    _jumlah_proyek_in_progress.postValue(NetworkResponse.SUCCESS(count))

                    _response_proyek_in_progress.postValue(NetworkResponse.SUCCESS(responseProgress.body()!!))
                } else {
                    _proyek_in_progress.postValue(NetworkResponse.ERROR("Gagal memuat proyek berjalan"))
                    _jumlah_proyek_in_progress.postValue(NetworkResponse.ERROR("Gagal memuat jumlah proyek berjalan"))
                    _response_proyek_in_progress.postValue(NetworkResponse.ERROR("Gagal memuat jumlah proyek berjalan"))
                }

            } catch (e: Exception) {
                Log.e("ManagerViewModel", "Get All Data Proyek error", e)
                val errorMsg = "Kesalahan jaringan atau server tidak merespons"
                _proyek_done.postValue(NetworkResponse.ERROR(errorMsg))
                _proyek_in_progress.postValue(NetworkResponse.ERROR(errorMsg))
                _jumlah_proyek_done.postValue(NetworkResponse.ERROR(errorMsg))
                _jumlah_proyek_in_progress.postValue(NetworkResponse.ERROR(errorMsg))
            }
        }
    }

    fun getDataProyekById(id: Int) {
        _response_by_id_proyek.postValue(NetworkResponse.LOADING)
        viewModelScope.launch {
            try {
                val response = managerRepository.getDataProyekById(id)

                if(response.isSuccessful && response.body() != null) {
                    _response_by_id_proyek.postValue(NetworkResponse.SUCCESS(response.body()!!))

                } else {
                    if(response.code() == 404) {
                        _response_by_id_proyek.postValue(NetworkResponse.ERROR("Data tidak ditemukan atau user tidak memiliki divisi"))
                    }
                }
            } catch (e : Exception) {
                Log.d("PROYEK_BY_ID", e.message.toString())
                _response_by_id_proyek.postValue(NetworkResponse.ERROR("Kesalahan jaringan atau server tidak merespons"))
            }
        }
    }

    fun addDataProject(addProjectRequest: AddProjectRequest) {
        _response_add_project.postValue(NetworkResponse.LOADING)
        viewModelScope.launch {
            try {
                val response = managerRepository.addDataProyek(addProjectRequest)

                if(response.isSuccessful && response.body() != null) {
                    _response_add_project.postValue(NetworkResponse.SUCCESS(response.body()!!))
                } else if(response.code() in  400 .. 499) {
                    _response_add_project.postValue(NetworkResponse.ERROR(response.message()))
                }
            } catch (e : Exception) {
                Log.d("ADD_PROYEK", e.message.toString())
                _response_add_project.postValue(NetworkResponse.ERROR("Kesalahan jaringan atau server tidak merespons"))
            }

        }
    }

    fun updateProject(id: Int, updateProjectRequest: UpdateProjectRequest) {
        _response_update_project.postValue(NetworkResponse.LOADING)
        viewModelScope.launch {
            try {
                val response = managerRepository.updateDataProyek(id, updateProjectRequest)

                if(response.isSuccessful && response.body() != null) {
                    _response_update_project.postValue(NetworkResponse.SUCCESS(response.body()!!))
                } else if(response.code() in  400 .. 499) {
                    _response_update_project.postValue(NetworkResponse.ERROR(response.message()))
                }
            } catch (e : Exception) {
                Log.d("ADD_PROYEK", e.message.toString())
                _response_update_project.postValue(NetworkResponse.ERROR("Kesalahan jaringan atau server tidak merespons"))
            }

        }
    }

    fun getTugasByIdProyek(id: Int) {
        _response_tugas_by_id_project.postValue(NetworkResponse.LOADING)
        viewModelScope.launch {
            try {
                val response = managerRepository.getTugasByIdProyek(id)

                if(response.isSuccessful && response.body() != null) {
                    _response_tugas_by_id_project.postValue(NetworkResponse.SUCCESS(response.body()!!))
                } else if(response.code() in  400 .. 499) {
                    _response_tugas_by_id_project.postValue(NetworkResponse.ERROR(response.message()))
                }
            } catch ( e : Exception) {
                Log.d("ADD_PROYEK", e.message.toString())
                _response_tugas_by_id_project.postValue(NetworkResponse.ERROR("Kesalahan jaringan atau server tidak merespons"))
            }
        }
    }

    fun addTugasToProyek(id: Int, projectAddTaskRequest: ProjectAddTaskRequest) {
        _response_add_tugas.postValue(NetworkResponse.LOADING)
        viewModelScope.launch {
            try {
                val response = managerRepository.addTugasToProyek(id, projectAddTaskRequest)

                if(response.isSuccessful && response.body() != null) {
                    _response_add_tugas.postValue(NetworkResponse.SUCCESS(response.body()!!))
                } else if(response.code() in  400 .. 499) {
                    _response_add_tugas.postValue(NetworkResponse.ERROR(response.message()))
                }
            } catch (e : Exception) {
                Log.d("ADD_PROYEK", e.message.toString())
                _response_add_tugas.postValue(NetworkResponse.ERROR("Kesalahan jaringan atau server tidak merespons"))
            }
        }
    }

    fun getKaryawanInDivisi() {
        _response_karyawan_in_divisi.postValue(NetworkResponse.LOADING)
        viewModelScope.launch {
            try {
                val response = managerRepository.getAllKaryawanDivisi()

                if(response.isSuccessful && response.body() != null) {
                    _response_karyawan_in_divisi.postValue(NetworkResponse.SUCCESS(response.body()!!))

                } else if(response.code() in  400 .. 499) {
                    _response_karyawan_in_divisi.postValue(NetworkResponse.ERROR(response.message()))
                }
            } catch (e : Exception) {
                Log.d("ADD_PROYEK", e.message.toString())
                _response_karyawan_in_divisi.postValue(NetworkResponse.ERROR("Kesalahan jaringan atau server tidak merespons"))
            }
        }
    }

    fun updateTugas(id: Int, projectUpdateTaskRequest: ProjectUpdateTaskRequest) {
        _response_update_tugas.postValue(NetworkResponse.LOADING)
        viewModelScope.launch {
            try {
                val response = managerRepository.updateDataTugas(id, projectUpdateTaskRequest)

                if(response.isSuccessful && response.body() != null) {
                    _response_update_tugas.postValue(NetworkResponse.SUCCESS(response.body()!!))
                } else if(response.code() in  400 .. 499) {
                    _response_update_tugas.postValue(NetworkResponse.ERROR(response.message()))
                }
            } catch (e : Exception) {
                _response_update_tugas.postValue(NetworkResponse.ERROR("Kesalahan jaringan atau server tidak merespons"))
            }
        }
    }

    fun getTugasByIdTugas(id: Int) {
        _response_tugas_by_id_tugas.postValue(NetworkResponse.LOADING)
        viewModelScope.launch {
            try {
                val response = managerRepository.getTugasByIdTugas(id)

                if(response.isSuccessful && response.body() != null) {
                    _response_tugas_by_id_tugas.postValue(NetworkResponse.SUCCESS(response.body()!!))
                } else if(response.code() in  400 .. 499) {
                    _response_tugas_by_id_tugas.postValue(NetworkResponse.ERROR(response.message()))
                }
            } catch ( e : Exception) {
                Log.d("ADD_PROYEK", e.message.toString())
                _response_tugas_by_id_tugas.postValue(NetworkResponse.ERROR("Kesalahan jaringan atau server tidak merespons"))
            }
        }
    }

    fun getTugasByIdTugasWithBukti(id: Int) {
        _response_tugas_by_id_tugas_with_bukti.postValue(NetworkResponse.LOADING)
        viewModelScope.launch {
            try {
                val response = managerRepository.getTugasByIdTugasWithBukti(id)

                if(response.isSuccessful && response.body() != null) {
                    _response_tugas_by_id_tugas_with_bukti.postValue(NetworkResponse.SUCCESS(response.body()!!))
                } else if(response.code() in  400 .. 499) {
                    _response_tugas_by_id_tugas_with_bukti.postValue(NetworkResponse.ERROR(response.message()))
                }
            } catch ( e : Exception) {
                Log.d("ADD_PROYEK", e.message.toString())
                _response_tugas_by_id_tugas_with_bukti.postValue(NetworkResponse.ERROR("Kesalahan jaringan atau server tidak merespons"))
            }
        }
    }

    fun updateStatusTugas(id: Int, updateStatusTaskAndProjectRequest: UpdateStatusTaskAndProjectRequest) {
        _response_update_status_tugas.postValue(NetworkResponse.LOADING)
        viewModelScope.launch {
            try {
                val response = managerRepository.updateStatusTugas(id, updateStatusTaskAndProjectRequest)

                if(response.isSuccessful && response.body() != null) {
                    _response_update_status_tugas.postValue(NetworkResponse.SUCCESS(response.body()!!))
                } else if(response.code() in  400 .. 499) {
                    _response_update_status_tugas.postValue(NetworkResponse.ERROR(response.message()))
                }
            } catch ( e : Exception) {
                Log.d("UPDATE_STATUS_TUGAS", e.message.toString())
                _response_update_status_tugas.postValue(NetworkResponse.ERROR("Kesalahan jaringan atau server tidak merespons"))
            }
        }
    }

    fun updateStatusProyek(id: Int, updateStatusTaskAndProjectRequest: UpdateStatusTaskAndProjectRequest) {
        _response_update_status_proyek.postValue(NetworkResponse.LOADING)
        viewModelScope.launch {
            try {
                val response = managerRepository.updateStatusProyek(id, updateStatusTaskAndProjectRequest)

                if(response.isSuccessful && response.body() != null) {
                    _response_update_status_proyek.postValue(NetworkResponse.SUCCESS(response.body()!!))
                } else if(response.code() in  400 .. 499) {
                    _response_update_status_proyek.postValue(NetworkResponse.ERROR(response.message()))
                }
            } catch ( e : Exception) {
                Log.d("UPDATE_STATUS_TUGAS", e.message.toString())
                _response_update_status_proyek.postValue(NetworkResponse.ERROR("Kesalahan jaringan atau server tidak merespons"))
            }
        }
    }


}
