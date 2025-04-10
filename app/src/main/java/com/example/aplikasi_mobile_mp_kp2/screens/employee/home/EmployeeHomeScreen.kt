package com.example.aplikasi_mobile_mp_kp2.screens.employee.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.aplikasi_mobile_mp_kp2.data.remote.SharedPrefsManager
import com.example.aplikasi_mobile_mp_kp2.navigation.Routes
import kotlinx.coroutines.delay

@Composable
fun EmployeeHomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    tugasHariIni: Task = Task(
        title = "Desain Brosur Promo",
        status = "In Progress",
        deadline = "9 April"
    ),
    notifikasi: List<String> = listOf("Tugas ‘Proposal Marketing’ sudah diverifikasi")
) {
    val context = LocalContext.current
    val sharedPrefsManager = SharedPrefsManager(context)

    val employeeName = sharedPrefsManager.getName()
    val divisiName = sharedPrefsManager.getDivisi()

    val alreadyNavigated = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!alreadyNavigated.value && sharedPrefsManager.getToken() == null) {
            alreadyNavigated.value = true
            navController.navigate(Routes.AuthGraph.route) {
                popUpTo(0) { inclusive = true } // Bersihin semua
                launchSingleTop = true
            }
        }
    }

    LaunchedEffect(true) {
        delay(100) // opsional, beri waktu sistem stabil
        if (sharedPrefsManager.getToken() == null) {
            navController.navigate(Routes.AuthGraph.route) {
                popUpTo(0) { inclusive = true } // Hapus semua history biar gak balik lagi
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            text = "👋 Hai, $employeeName!",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Divisi: $divisiName",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // CARD STATUS
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem("Aktif", 4)
                StatItem("In Progress", 2)
                StatItem("Menunggu Verifikasi", 1)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // TUGAS HARI INI
        Text(
            text = "📌 Tugas Hari Ini:",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = tugasHariIni.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = tugasHariIni.status,
                    style = MaterialTheme.typography.labelMedium.copy(color = Color.Blue)
                )
                Text(
                    text = "Deadline: ${tugasHariIni.deadline}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // QUICK ACTIONS
        Text(
            text = "🔘 Quick Action",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { /* TODO: Navigate ke daftar tugas */ },
                modifier = Modifier.weight(1f)
            ) {
                Text("Lihat Semua Tugas")
            }

            Button(
                onClick = { /* TODO: Navigate ke upload bukti */ },
                modifier = Modifier.weight(1f)
            ) {
                Text("Upload Bukti")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // NOTIFIKASI
        Text(
            text = "📣 Notifikasi:",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        notifikasi.forEach { notif ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9))
            ) {
                Text(
                    text = notif,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun StatItem(label: String, count: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "$count", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(text = label, style = MaterialTheme.typography.bodySmall)
    }
}

data class Task(
    val title: String,
    val status: String,
    val deadline: String
)
