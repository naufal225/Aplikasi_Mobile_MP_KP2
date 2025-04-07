package com.example.aplikasi_mobile_mp_kp2.screens.manager.poject

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.aplikasi_mobile_mp_kp2.data.model.Proyek
import com.example.aplikasi_mobile_mp_kp2.data.model.Tugas
import com.example.aplikasi_mobile_mp_kp2.data.model.TugasResponse
import com.example.aplikasi_mobile_mp_kp2.data.remote.NetworkResponse
import com.example.aplikasi_mobile_mp_kp2.navigation.Routes
import com.example.aplikasi_mobile_mp_kp2.viewmodel.manager.ManagerViewModel

@Composable
fun ManagerProjectDetailScreen(
    projectId: String,
    modifier: Modifier = Modifier,
    managerViewModel: ManagerViewModel = viewModel(),
    navController: NavController
) {
    val id = projectId.toIntOrNull()
    val response by managerViewModel.response_by_id_proyek.observeAsState()
    val tugasResponse by managerViewModel.response_tugas_by_id_project.observeAsState()

    LaunchedEffect(id) {
        if (id != null) {
            managerViewModel.getDataProyekById(id)
            managerViewModel.getTugasByIdProyek(id)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when (response) {
            is NetworkResponse.LOADING -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
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
                            }
                        )
                    }

                    is NetworkResponse.LOADING -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    is NetworkResponse.ERROR -> {
                        Text(
                            text = (tugasResponse as NetworkResponse.ERROR).message ?: "Gagal memuat tugas",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    else -> {
                        Text("Tidak ada data tugas", modifier = Modifier.align(Alignment.Center))
                    }
                }

            }

            is NetworkResponse.ERROR -> {
                Text(
                    text = (response as NetworkResponse.ERROR).message ?: "Terjadi kesalahan",
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
                Text("Tidak ada data", modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailContent(
    proyek: Proyek,
    listTugas: List<TugasResponse>,
    onUpdateClick: () -> Unit
) {
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
            Text(proyek.namaProyek, style = MaterialTheme.typography.headlineSmall)

            Button(
                onClick = onUpdateClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Update")
            }
        }

        Text("Status: ${proyek.status}")
        LinearProgressIndicator(
            progress = proyek.progress / 100f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        Text("Progress: ${proyek.progress}%", modifier = Modifier.align(Alignment.End))

        Spacer(modifier = Modifier.height(16.dp))

        // Tab tugas
        val tabItems = listOf("Pending", "In Progress", "Waiting Review", "Done")

        TabRow(selectedTabIndex = selectedTabIndex) {
            tabItems.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val filteredTugas = when (selectedTabIndex) {
            0 -> listTugas.filter { it.status == "pending" }
            1 -> listTugas.filter { it.status == "in-progress" }
            2 -> listTugas.filter { it.status == "waiting-for-review" }
            3 -> listTugas.filter { it.status == "done" }
            else -> listTugas
        }

        if (filteredTugas.isEmpty()) {
            Text("Tidak ada tugas", modifier = Modifier.padding(16.dp))
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
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(tugas.namaTugas, style = MaterialTheme.typography.bodyLarge)

                        val statusColor = when (tugas.status) {
                            "pending" -> MaterialTheme.colorScheme.tertiary
                            "in-progress" -> MaterialTheme.colorScheme.primary
                            "waiting-for-review" -> MaterialTheme.colorScheme.secondary
                            "done" -> MaterialTheme.colorScheme.primaryContainer
                            else -> MaterialTheme.colorScheme.outline
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
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(selectedTask!!.namaTugas, style = MaterialTheme.typography.titleLarge)
                    Text("Status: ${selectedTask!!.status}", style = MaterialTheme.typography.labelLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Deskripsi:", style = MaterialTheme.typography.titleMedium)
                    Text(selectedTask!!.deskripsiTugas ?: "Tidak ada deskripsi")
                }
            }
        }
    }
}