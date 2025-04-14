package com.example.aplikasi_mobile_mp_kp2.screens.employee.project

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.aplikasi_mobile_mp_kp2.data.model.Proyek
import com.example.aplikasi_mobile_mp_kp2.data.model.Tugas
import com.example.aplikasi_mobile_mp_kp2.data.model.TugasResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.UpdateStatusTaskAndProjectRequest
import com.example.aplikasi_mobile_mp_kp2.data.remote.NetworkResponse
import com.example.aplikasi_mobile_mp_kp2.data.remote.SharedPrefsManager
import com.example.aplikasi_mobile_mp_kp2.navigation.Routes
import com.example.aplikasi_mobile_mp_kp2.viewmodel.manager.ManagerViewModel

// Define a dark gray color for consistent use throughout the UI
private val DarkGray = Color(0xFF333333)
private val LightGray = Color(0xFFF5F5F5)

@Composable
fun EmployeeProjectDetailScreen(
    projectId: String,
    modifier: Modifier = Modifier,
    managerViewModel: ManagerViewModel = viewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val id = projectId.toIntOrNull()
    val response by managerViewModel.response_by_id_proyek.observeAsState()
    val tugasResponse by managerViewModel.response_tugas_by_id_project.observeAsState()

    val sharedPrefsManager = SharedPrefsManager(context)
    val idUser = sharedPrefsManager.getId()

    val updateStatusTugasResponse by managerViewModel.response_update_status_tugas.observeAsState()

    LaunchedEffect(id) {
        if (id != null) {
            managerViewModel.getDataProyekById(id)
            managerViewModel.getTugasByIdProyek(id)
        }
    }

    LaunchedEffect(updateStatusTugasResponse) {
        when(updateStatusTugasResponse) {
            is NetworkResponse.ERROR -> {
                Log.e("ERROR_UPDATE_STATUS_TUGAS", "Err")
            }
            NetworkResponse.LOADING -> {}
            is NetworkResponse.SUCCESS -> {
                navController.navigate(Routes.EmployeeProjectDetail.employeeProjectDetailWithId(projectId))
                managerViewModel.response_update_status_tugas.postValue(null)
                Toast.makeText(context, "Berhasil Update Status", Toast.LENGTH_SHORT).show()
            }
            null -> {}
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        when (response) {
            is NetworkResponse.LOADING -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Black
                )
            }

            is NetworkResponse.SUCCESS -> {
                val proyek = (response as NetworkResponse.SUCCESS).data.data.proyek
                when (tugasResponse) {
                    is NetworkResponse.SUCCESS -> {
                        val listTugas = (tugasResponse as NetworkResponse.SUCCESS).data.data.tugas
                        ProjectDetailContent(
                            idUser = idUser!!,
                            proyek = proyek,
                            listTugas = listTugas,
                            navController = navController,
                            managerViewModel = managerViewModel,
                        )
                    }

                    is NetworkResponse.LOADING -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.Black
                        )
                    }

                    is NetworkResponse.ERROR -> {
                        Text(
                            text = (tugasResponse as NetworkResponse.ERROR).message ?: "Gagal memuat tugas",
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.Black
                        )
                    }

                    else -> {
                        Text(
                            "Tidak ada data tugas",
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.Black
                        )
                    }
                }
            }

            is NetworkResponse.ERROR -> {
                Text(
                    text = (response as NetworkResponse.ERROR).message ?: "Terjadi kesalahan",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Black
                )
            }

            else -> {
                Text(
                    "Tidak ada data",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Black
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailContent(
    idUser: String,
    proyek: Proyek,
    listTugas: List<TugasResponse>,
    navController: NavController,
    managerViewModel: ManagerViewModel,
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var showTaskDialog by remember { mutableStateOf(false) }
    var selectedTask by remember { mutableStateOf<TugasResponse?>(null) }
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Header with project info
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                proyek.namaProyek,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            "Status: ${proyek.status}",
            color = DarkGray
        )

        LinearProgressIndicator(
            progress = proyek.progress / 100f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            color = Color.Black,
            trackColor = LightGray
        )

        Text(
            "Progress: ${proyek.progress}%",
            modifier = Modifier.align(Alignment.End),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tab tugas
        val tabItems = listOf("Pending", "In Progress", "Waiting Review", "Done")

        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = LightGray,
            contentColor = Color.Black,
            indicator = { /* Remove default indicator */ }
        ) {
            tabItems.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            title,
                            color = if (selectedTabIndex == index) Color.Black else DarkGray
                        )
                    },
                    modifier = Modifier.background(
                        if (selectedTabIndex == index) Color.White else LightGray
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val filteredTugas = when (selectedTabIndex) {
            0 -> listTugas.filter { it.status == "pending" }
            1 -> listTugas.filter { it.status == "in-progress" }
            2 -> listTugas.filter { it.status == "waiting_for_review" }
            3 -> listTugas.filter { it.status == "done" }
            else -> listTugas
        }

        if (filteredTugas.isEmpty()) {
            Text(
                "Tidak ada tugas",
                modifier = Modifier.padding(16.dp),
                color = DarkGray
            )
        } else {
            filteredTugas.forEach { tugas ->
                val isUserSTask = idUser == tugas.idKaryawan.toString()
                val cardColor = if (isUserSTask) {
                    Color(0xFF4CAF50).copy(alpha = 0.2f) // Lighter green
                } else {
                    LightGray
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            selectedTask = tugas
                            showTaskDialog = true
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = cardColor
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            tugas.namaTugas,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black
                        )

                        val statusColor = when (tugas.status) {
                            "pending" -> Color.Black.copy(alpha = 0.5f)
                            "in-progress" -> Color.Black
                            "waiting_for_review" -> DarkGray
                            "done" -> Color.Black
                            else -> DarkGray.copy(alpha = 0.5f)
                        }

                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(statusColor, RoundedCornerShape(6.dp))
                        )
                    }
                }
            }
        }
    }

    if (showTaskDialog && selectedTask != null) {
        val isUserSTask = selectedTask!!.idKaryawan.toString() == idUser
        Dialog(
            onDismissRequest = { showTaskDialog = false },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth()
                ) {
                    // Header with task name
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            selectedTask!!.namaTugas,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.weight(1f),
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Column (
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            "Status: ${selectedTask!!.status.replace("_", " ").replace("-"," ").capitalize()}",
                            style = MaterialTheme.typography.labelLarge,
                            color = DarkGray
                        )
                        Text(
                            "Penanggung Jawab: ${selectedTask!!.karyawan.nama}",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "Deskripsi:",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black
                    )
                    Text(
                        selectedTask!!.deskripsiTugas ?: "Tidak ada deskripsi",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp),
                        color = DarkGray
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "Tenggat Waktu: ${selectedTask!!.tenggatWaktu}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DarkGray
                    )

                    // Spacer untuk pisah dengan button bawah
                    Spacer(modifier = Modifier.height(24.dp))

                    if(isUserSTask) {
                        // Conditional action button
                        when (selectedTask!!.status) {
                            "pending" -> {
                                Button(
                                    onClick = {
                                        managerViewModel.updateStatusTugas(selectedTask!!.id, UpdateStatusTaskAndProjectRequest("in-progress"))
                                        managerViewModel.response_update_status_tugas.postValue(null)
                                        Toast.makeText(context, "Berhasil buat tugas in-progress", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Black,
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text("Buat In-Progress")
                                }
                            }

                            "in-progress" -> {
                                if(proyek.status == "in-progress") {
                                    Button(
                                        onClick = {
                                            navController.navigate(Routes.EmployeeProjectAddBuktiTugas.employeeProjectAddBuktiTugas(selectedTask!!.id.toString()))
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.Black,
                                            contentColor = Color.White
                                        )
                                    ) {
                                        Text("Ajukan Bukti Selesai")
                                    }
                                }
                            }
                        }
                    }

                    // Add a dismiss button
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { showTaskDialog = false },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkGray,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Tutup")
                    }
                }
            }
        }
    }
}