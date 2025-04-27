package com.example.aplikasi_mobile_mp_kp2.screens.manager.home

import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplikasi_mobile_mp_kp2.data.model.NotificationData
import com.example.aplikasi_mobile_mp_kp2.data.model.ProyekProgressResponse
import com.example.aplikasi_mobile_mp_kp2.data.remote.NetworkResponse
import com.example.aplikasi_mobile_mp_kp2.data.remote.SharedPrefsManager
import com.example.aplikasi_mobile_mp_kp2.navigation.Routes
import com.example.aplikasi_mobile_mp_kp2.viewmodel.manager.ManagerViewModel
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

// Define a dark gray color
private val DarkGray = Color(0xFF333333)

@Composable
fun ManagerHomeScreen(managerViewModel: ManagerViewModel, modifier: Modifier = Modifier, navController: NavController) {
    val context = LocalContext.current
    val sharedPrefsManager = remember { SharedPrefsManager(context) }

    val jumlahProyekDone by managerViewModel.jumlah_proyek_done.observeAsState()
    val jumlahProyekInProgress by managerViewModel.jumlah_proyek_in_progress.observeAsState()

    val responseProyekDone by managerViewModel.response_proyek_done.observeAsState()
    val responseProyekInProgress by managerViewModel.response_proyek_in_progress.observeAsState()

    val responseNotif by managerViewModel.response_list_notif.observeAsState()
    val listNotifState = remember { mutableStateOf<List<NotificationData>>(emptyList()) }
    val managerName = sharedPrefsManager.getName()
    val divisionName = sharedPrefsManager.getDivisi()

    val token = remember { sharedPrefsManager.getToken() } // disimpan di remember

    LaunchedEffect(Unit) {
        managerViewModel.getNotification()
    }

    LaunchedEffect(token) {
        if (token == null) {
            navController.navigate(Routes.AuthGraph.route) {
                popUpTo(0) { inclusive = true } // clear semua
            }
        } else {
            managerViewModel.getDataAllProyek()
        }
    }

    LaunchedEffect(responseNotif) {
        when(responseNotif) {
            is NetworkResponse.ERROR -> {
                Log.d("ERROR_NOTIF", "")
            }
            NetworkResponse.LOADING -> {
                Log.d("LOADING_NOTIF", "")

            }
            is NetworkResponse.SUCCESS -> {
                val response = (responseNotif as NetworkResponse.SUCCESS).data
                listNotifState.value = response.data
                Log.d("BERHASIL", "")

            }
            else -> {

            }
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // Header
        Text(
            text = "👋 Selamat datang, $managerName",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = Color.Black
        )
        Text(
            text = "Divisi: $divisionName",
            style = MaterialTheme.typography.bodyMedium,
            color = DarkGray
        )

        Spacer(Modifier.height(20.dp))

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
                backgroundColor = Color.Black,
                textColor = Color.White,
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
                backgroundColor = DarkGray,
                textColor = Color.White,
                modifier = Modifier
                    .height(100.dp)
                    .weight(1f)
            )
        }

        Spacer(Modifier.height(28.dp))

        Text(
            text = "📣 Pemberitahuan:",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        listNotifState.value.forEach { notif ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    notif.pesan?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        notif.createdAt?.let { createdAt ->
                            Text(
                                text = formatTime(createdAt), // Jam
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                        notif.createdAt?.let { createdAt ->
                            Text(
                                text = formatDate(createdAt), // Tanggal
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }


    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = textColor
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = textColor
            )
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Spacer(Modifier.height(20.dp))
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        color = Color.Black
    )
    Spacer(Modifier.height(10.dp))
}

@Composable
fun PrettyList(items: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items.forEach {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = DarkGray),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Text(
                    text = it,
                    modifier = Modifier.padding(14.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }
        }
    }
}

fun formatDate(dateString: String): String {
    return try {
        val instant = Instant.parse(dateString)
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("id"))
        formatter.format(instant.atZone(ZoneId.systemDefault()))
    } catch (e: Exception) {
        "-"
    }
}

fun formatTime(dateString: String): String {
    return try {
        val instant = Instant.parse(dateString)
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        formatter.format(instant.atZone(ZoneId.systemDefault()))
    } catch (e: Exception) {
        "-"
    }
}