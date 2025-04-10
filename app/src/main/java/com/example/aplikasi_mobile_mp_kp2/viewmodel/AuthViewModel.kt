package com.example.aplikasi_mobile_mp_kp2.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.aplikasi_mobile_mp_kp2.data.model.LoginRequest
import com.example.aplikasi_mobile_mp_kp2.data.model.LoginResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.LogoutResponse
import com.example.aplikasi_mobile_mp_kp2.data.remote.NetworkResponse
import com.example.aplikasi_mobile_mp_kp2.data.remote.RetrofitInstance
import com.example.aplikasi_mobile_mp_kp2.data.remote.SharedPrefsManager
import com.example.aplikasi_mobile_mp_kp2.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val application: Application) : AndroidViewModel(application) {
    private val authApi = RetrofitInstance.getInstanceAuthInterface(application.applicationContext)
    private val authRepos = AuthRepository(authApi)

    private val sharedPrefsManager = SharedPrefsManager(application.applicationContext)

    private var _login_result = MutableLiveData<NetworkResponse<LoginResponse>>()
    private var _logout_result = MutableLiveData<NetworkResponse<LogoutResponse>>()

    val login_result = _login_result
    val logout_result = _logout_result

    fun checkToken() : Boolean {
        return sharedPrefsManager.getToken() != null
    }

    fun login(loginRequest: LoginRequest) {
        _login_result.postValue(NetworkResponse.LOADING)
        viewModelScope.launch {
            try {
                val response = authRepos.login(loginRequest)
                if(response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let { body ->
                        val divisi = body.divisi?.namaDivisi
                        body.user?.let { user ->
                            sharedPrefsManager.saveAddress(user.alamat)
                            sharedPrefsManager.saveName(user.namaLengkap)
                            sharedPrefsManager.saveUsername(user.username)
                            sharedPrefsManager.saveId(user.id)
                            if (divisi != null) {
                                sharedPrefsManager.saveDivisi(divisi)
                            }
                        }
                        body.token?.let { token ->
                            sharedPrefsManager.saveToken(token)
                        }
                        _login_result.postValue(NetworkResponse.SUCCESS(body))
                    }

                } else {
                    if(response.code() in 400..499) {
                        _login_result.postValue( NetworkResponse.ERROR("Kesalahan Autentikasi"))
                    } else if(response.code() in 500..599) {
                        _login_result.postValue(NetworkResponse.ERROR("Kesalahan server"))
                    }
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Login error", e)
                _login_result.postValue(NetworkResponse.ERROR("kesalahan jaringan"))
            }
        }
    }

    fun logout() {
        _logout_result.postValue(NetworkResponse.LOADING)
        viewModelScope.launch {
            try {
                val response = authRepos.logout()
                if(response.isSuccessful) {
                    response.body()?.let {
                        _logout_result.postValue(NetworkResponse.SUCCESS(it))

                    }
                    sharedPrefsManager.clearSharedPref()
                } else {
                    if(response.code() in 400..499) {
                        _logout_result.postValue( NetworkResponse.ERROR("Kesalahan Autentikasi"))
                    } else if(response.code() in 500..599) {
                        _logout_result.postValue(NetworkResponse.ERROR("Kesalahan server"))
                    }
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Logout error", e)
                _logout_result.postValue(NetworkResponse.ERROR("kesalahan jaringan"))
            }
        }
    }

}