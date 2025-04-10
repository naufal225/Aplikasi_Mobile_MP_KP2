package com.example.aplikasi_mobile_mp_kp2.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.aplikasi_mobile_mp_kp2.data.remote.SharedPrefsManager
import com.example.aplikasi_mobile_mp_kp2.navigation.Routes
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavHostController,
    sharedPrefsManager: SharedPrefsManager,
) {
    LaunchedEffect(Unit) {
        delay(500) // Animasi/splash sebentar
        val token = sharedPrefsManager.getToken()
        if (token == null) {
            navController.navigate(Routes.AuthGraph.route) {
                popUpTo(0) { inclusive = true }
            }
        } else {
            // Cek role di sini kalau perlu
            navController.navigate(Routes.ManagerGraph.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    // Tampilan loading / logo animasi
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Memuat aplikasi...")
    }
}
