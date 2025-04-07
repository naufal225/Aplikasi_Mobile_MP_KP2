package com.example.aplikasi_mobile_mp_kp2.screens.manager.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.aplikasi_mobile_mp_kp2.data.model.ProyekProgressResponse
import com.example.aplikasi_mobile_mp_kp2.data.remote.NetworkResponse
import com.example.aplikasi_mobile_mp_kp2.data.remote.SharedPrefsManager
import com.example.aplikasi_mobile_mp_kp2.viewmodel.manager.ManagerViewModel

@Composable
fun ManagerHomeScreen(managerViewModel: ManagerViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val sharedPrefsManager = remember { SharedPrefsManager(context) }

    val jumlahProyekDone by managerViewModel.jumlah_proyek_done.observeAsState()
    val jumlahProyekInProgress by managerViewModel.jumlah_proyek_in_progress.observeAsState()

    val responseProyekDone by managerViewModel.response_proyek_done.observeAsState()
    val responseProyekInProgress by managerViewModel.response_proyek_in_progress.observeAsState()

    val managerName = sharedPrefsManager.getName()
    val divisionName = sharedPrefsManager.getDivisi()

    LaunchedEffect(Unit) {
        managerViewModel.getDataAllProyek() // fetch saat composable muncul
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header
        Text("Halo, $managerName 👋", style = MaterialTheme.typography.headlineSmall)
        Text("Divisi: $divisionName", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)

        Spacer(Modifier.height(16.dp))

        // Stat Cards
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            StatCard(
                title = "Proyek Aktif",
                value = when (responseProyekInProgress) {
                    is NetworkResponse.SUCCESS -> {
                        val response = (responseProyekInProgress as NetworkResponse.SUCCESS<ProyekProgressResponse>).data
                        response.data.jumlah.toString()
                    }
                    is NetworkResponse.LOADING -> "..."
                    is NetworkResponse.ERROR -> "?"
                    else -> "-"
                },
                modifier = Modifier
                    .height(100.dp)
                    .weight(1f)
            )

            StatCard(
                title = "Proyek Selesai",
                value = when (responseProyekDone) {
                    is NetworkResponse.SUCCESS -> {
                        val response = (responseProyekDone as NetworkResponse.SUCCESS<ProyekProgressResponse>).data
                        response.data.jumlah.toString()
                    }
                    is NetworkResponse.LOADING -> "..."
                    is NetworkResponse.ERROR -> "?"
                    else -> "-"
                },
                modifier = Modifier
                    .height(100.dp)
                    .weight(1f)
            )

        }

        Spacer(Modifier.height(24.dp))

        // Sementara dummy list, nanti sambungkan ke data asli
        SectionHeader("Proyek Deadline Terdekat ⏰")
        DummyList(items = listOf("Aplikasi HR", "Website Company Profile"))

        SectionHeader("Verifikasi Tugas ⏳")
        DummyList(items = listOf("Tugas 1 dari Lina", "Tugas 2 dari Udin"))

        SectionHeader("Notifikasi 🚨")
        DummyList(items = listOf("Laporan proyek diterima", "Tugas belum diverifikasi"))
    }
}


@Composable
fun StatCard(title: String, value: String, modifier: Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(title, style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(8.dp))
            Text(value, style = MaterialTheme.typography.headlineSmall)
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Spacer(Modifier.height(16.dp))
    Text(title, style = MaterialTheme.typography.titleMedium)
    Spacer(Modifier.height(8.dp))
}

@Composable
fun DummyList(items: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items.forEach {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Text(it, modifier = Modifier.padding(12.dp))
            }
        }
    }
}

@Composable
fun QuickActionButton(label: String, modifier: Modifier = Modifier) {
    Button(onClick = { /* TODO */ }, modifier = modifier) {
        Text(label)
    }
}
