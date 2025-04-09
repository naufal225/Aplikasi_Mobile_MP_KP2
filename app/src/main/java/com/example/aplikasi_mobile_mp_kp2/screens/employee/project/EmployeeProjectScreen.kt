package com.example.aplikasi_mobile_mp_kp2.screens.employee.project

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.aplikasi_mobile_mp_kp2.data.model.ProyekProgressResponse
import com.example.aplikasi_mobile_mp_kp2.data.remote.NetworkResponse
import com.example.aplikasi_mobile_mp_kp2.navigation.Routes
import com.example.aplikasi_mobile_mp_kp2.viewmodel.manager.ManagerViewModel

@Composable
fun EmployeeProjectScreen(
    managerViewModel: ManagerViewModel, // Gunakan ViewModel yang sama jika data diambil dari endpoint yang sama
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val proyekList by managerViewModel.response_all_proyek.observeAsState()
    var selectedStatus by remember { mutableStateOf("in-progress") }
    val statusTabs = listOf("pending", "in-progress", "waiting_for_review", "done")

    LaunchedEffect(Unit) {
        managerViewModel.getDataAllProyek()
    }

    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Proyek Divisi", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))

        // Tab Bar untuk status proyek
        TabRow(selectedTabIndex = statusTabs.indexOf(selectedStatus)) {
            statusTabs.forEachIndexed { _, status ->
                Tab(
                    selected = selectedStatus == status,
                    onClick = { selectedStatus = status },
                    text = { Text(status.replace("_", " ").capitalize()) }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        when (proyekList) {
            is NetworkResponse.LOADING -> {
                Text("Memuat data proyek...")
            }
            is NetworkResponse.ERROR -> {
                val res = proyekList
                Log.e("EMPLOYEE_ALL_PROYEK", res.toString())
                Text("Terjadi kesalahan: ${(proyekList as NetworkResponse.ERROR).message}")
            }
            is NetworkResponse.SUCCESS -> {
                val data = (proyekList as NetworkResponse.SUCCESS<ProyekProgressResponse>).data.data.proyek
                val filtered = data.filter { it.status == selectedStatus }

                if (filtered.isEmpty()) {
                    Text("Tidak ada proyek dengan status '$selectedStatus'")
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(filtered) { proyek ->
                            ElevatedCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        navController.navigate(Routes.EmployeeProjectDetail.employeeProjectDetailWithId(proyek.id.toString()))
                                    },
                                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                                colors = CardDefaults.elevatedCardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            proyek.namaProyek,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Bold
                                        )

                                        val statusColor = when(proyek.status.lowercase()) {
                                            "selesai" -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
                                            "dalam pengerjaan" -> MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
                                            "tertunda" -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
                                            else -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
                                        }

                                        Surface(
                                            shape = RoundedCornerShape(16.dp),
                                            color = statusColor.first,
                                        ) {
                                            Text(
                                                proyek.status,
                                                style = MaterialTheme.typography.labelSmall,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                color = statusColor.second
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        "Deskripsi:",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        proyek.deskripsiProyek,
                                        style = MaterialTheme.typography.bodySmall,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    if (proyek.progress != null) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            LinearProgressIndicator(
                                                progress = { proyek.progress.toFloat() / 100f },
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .height(6.dp),
                                                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                            Text(
                                                "${proyek.progress}%",
                                                style = MaterialTheme.typography.labelSmall,
                                                modifier = Modifier.padding(start = 8.dp),
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.CalendarToday,
                                                contentDescription = "Deadline",
                                                modifier = Modifier.size(16.dp),
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                "Deadline: ${proyek.tenggatWaktu}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }

                                        Icon(
                                            imageVector = Icons.Outlined.ChevronRight,
                                            contentDescription = "View Details",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else -> Text("Belum ada data.")
        }
    }
}
