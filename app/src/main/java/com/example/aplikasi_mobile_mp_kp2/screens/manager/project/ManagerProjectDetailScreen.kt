package com.example.aplikasi_mobile_mp_kp2.screens.manager.project

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.aplikasi_mobile_mp_kp2.data.model.Proyek
import com.example.aplikasi_mobile_mp_kp2.data.model.TugasResponse
import com.example.aplikasi_mobile_mp_kp2.data.model.UpdateStatusTaskAndProjectRequest
import com.example.aplikasi_mobile_mp_kp2.data.remote.NetworkResponse
import com.example.aplikasi_mobile_mp_kp2.navigation.Routes
import com.example.aplikasi_mobile_mp_kp2.viewmodel.manager.ManagerViewModel

// Define a dark gray color for consistent use throughout the UI
private val DarkGray = Color(0xFF333333)
private val LightGray = Color(0xFFF5F5F5)

@Composable
fun ManagerProjectDetailScreen(
    projectId: String,
    modifier: Modifier = Modifier,
    managerViewModel: ManagerViewModel = viewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val id = projectId.toIntOrNull()
    val response by managerViewModel.response_by_id_proyek.observeAsState()
    val tugasResponse by managerViewModel.response_tugas_by_id_project.observeAsState()

    val updateStatusTugasResponse by managerViewModel.response_update_status_tugas.observeAsState()
    val updateStatusProyekResponse by managerViewModel.response_update_status_proyek.observeAsState()

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
                navController.navigate(Routes.ManagerProjectDetail.managerProjectDetailWithId(projectId))
                managerViewModel.response_update_status_tugas.postValue(null)
                Toast.makeText(context, "Berhasil Update Status", Toast.LENGTH_SHORT).show()
            }
            null -> {}
        }
    }

    LaunchedEffect(updateStatusProyekResponse) {
        when(updateStatusProyekResponse) {
            is NetworkResponse.ERROR -> {
                val err = (updateStatusProyekResponse as NetworkResponse.ERROR).message
                Log.e("ERROR_UPDATE_STATUS_PROYEK", err)
            }
            NetworkResponse.LOADING -> {
                Log.d("LOADING_UPDATE_STATUS_PROYEK", "LOADING")
            }
            is NetworkResponse.SUCCESS -> {
                managerViewModel.response_update_status_proyek.postValue(null)
                Toast.makeText(context, "Berhasil ajukan review ke admin", Toast.LENGTH_SHORT).show()
                navController.navigate(Routes.ManagerProjectDetail.managerProjectDetailWithId(projectId))
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
                            proyek = proyek,
                            listTugas = listTugas,
                            onUpdateClick = {
                                navController.navigate(Routes.ManagerProjectUpdate.managerProjectUpdate(projectId))
                            },
                            onAddTaskClick = {
                                navController.navigate(Routes.ManagerAddTask.addTaskWithIdProject(projectId))
                            },
                            navController,
                            managerViewModel,
                            onUpdateProyekStatus = {
                                managerViewModel.updateStatusProyek(projectId.toInt(), UpdateStatusTaskAndProjectRequest("waiting_for_review"))
                            }
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
    proyek: Proyek,
    listTugas: List<TugasResponse>,
    onUpdateClick: () -> Unit,
    onAddTaskClick: () -> Unit,
    navController: NavController,
    managerViewModel: ManagerViewModel,
    onUpdateProyekStatus: () -> Unit
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
        // Header with project info and update button
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
                color = Color.Black
            )

            if (proyek.status != "waiting_for_review" && proyek.status != "done") {
                Button(
                    onClick = onUpdateClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text("Update")
                }
            }
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

        if(proyek.status == "pending" || proyek.status == "in-progress") {
            Button(
                onClick = onAddTaskClick,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkGray,
                    contentColor = Color.White
                )
            ) {
                Text("Tambah Tugas")
            }
        }

        if (filteredTugas.isEmpty()) {
            Text(
                "Tidak ada tugas",
                modifier = Modifier.padding(16.dp),
                color = DarkGray
            )
        } else {
            filteredTugas.forEach { tugas ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            selectedTask = tugas
                            showTaskDialog = true
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = LightGray
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

            if (proyek.progress == 100 && proyek.status == "in-progress") {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onUpdateProyekStatus,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text("Ajukan Review Project ke Admin")
                }
            }
        }
    }

    if (showTaskDialog && selectedTask != null) {
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
                    // Header with task name and edit button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            selectedTask!!.namaTugas,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.weight(1f),
                            color = Color.Black
                        )

                        if(proyek.status != "done" && proyek.status != "waiting_for_review" && selectedTask!!.status != "done" && selectedTask!!.status != "waiting_for_review") {
                            Button(
                                onClick = {
                                    showTaskDialog = false
                                    navController.navigate(Routes.ManagerProjectUpdateTask.managerProjectUpdateTask(taskId = selectedTask!!.id.toString()))
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Black,
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Edit")
                            }
                        }
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

                    if(proyek.status != "done") {
                        when (selectedTask!!.status) {
                            "pending" -> {
                                Button(
                                    onClick = {
                                        managerViewModel.updateStatusTugas(selectedTask!!.id, UpdateStatusTaskAndProjectRequest("in-progress"))
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

                            "waiting_for_review" -> {
                                Button(
                                    onClick = {
                                        navController.navigate(Routes.ManagerProjectBuktiTask.managerProjectBuktiTask(selectedTask!!.id.toString()))
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = DarkGray,
                                        contentColor = Color.White
                                    ),
                                    enabled = selectedTask != null && selectedTask!!.id != null,
                                ) {
                                    Text("Lihat Bukti Pengerjaan")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}