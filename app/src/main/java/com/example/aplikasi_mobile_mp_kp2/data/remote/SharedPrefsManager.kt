package com.example.aplikasi_mobile_mp_kp2.data.remote

import android.content.Context

class SharedPrefsManager(val context: Context) {
    val sharedPreferences = context.getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        sharedPreferences.edit().putString("USER_TOKEN", token).apply()
    }

    fun getToken() : String? {
        return sharedPreferences.getString("USER_TOKEN", null)
    }

    fun saveName(name: String) {
        sharedPreferences.edit().putString("USER_NAME", name).apply()
    }

    fun getName() : String? {
        return sharedPreferences.getString("USER_NAME", null)
    }

    fun saveUsername(username: String) {
        sharedPreferences.edit().putString("USER_NAME", username).apply()
    }

    fun getUsername() : String? {
        return sharedPreferences.getString("USER_NAME", null)
    }

    fun saveAddress(address: String) {
        sharedPreferences.edit().putString("USER_ADDRESS", address).apply()
    }

    fun getAddress() : String? {
        return sharedPreferences.getString("USER_ADDRESS", null)
    }

    fun saveTipe(tipe: String) {
        return sharedPreferences.edit().putString("USER_TIPE", tipe).apply()
    }

    fun getTipe() : String? {
        return sharedPreferences.getString("USER_TIPE", null)
    }

    fun clearSharedPref() {
        sharedPreferences.edit().clear().apply()
    }
}