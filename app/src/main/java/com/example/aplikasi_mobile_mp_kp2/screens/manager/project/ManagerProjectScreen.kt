package com.example.aplikasi_mobile_mp_kp2.screens.manager.project

import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.aplikasi_mobile_mp_kp2.data.model.ProyekProgressResponse
import com.example.aplikasi_mobile_mp_kp2.data.remote.NetworkResponse
import com.example.aplikasi_mobile_mp_kp2.navigation.Routes
import com.example.aplikasi_mobile_mp_kp2.viewmodel.manager.ManagerViewModel

// Define a dark gray color for consistent use throughout the UI
private val DarkGray = Color(0xFF333333)
private val LightGray = Color(0xFFF5F5F5)

@Composable
fun ManagerProyekScreen(
    managerViewModel: ManagerViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val proyekList by managerViewModel.response_all_proyek.observeAsState()
    var selectedStatus by remember { mutableStateOf("in-progress") }
    val context = LocalContext.current

    val statusTabs = listOf("pending", "in-progress", "waiting_for_review", "done")
    val tabItems = listOf("Pending", "In Progress", "Waiting Review", "Done")

    var selectedTabIndex by remember { mutableIntStateOf(1) } // Default to "In Progress"

    LaunchedEffect(Unit) {
        managerViewModel.getDataAllProyek()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            "Daftar Proyek",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))

        // Tab Bar untuk status proyek
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = LightGray,
            contentColor = Color.Black,
            indicator = { /* Remove default indicator */ }
        ) {
            tabItems.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        selectedTabIndex = index
                        selectedStatus = statusTabs[index]
                    },
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

        Spacer(Modifier.height(16.dp))

        // Tombol Tambah Proyek
        Button(
            onClick = {
                navController.navigate(Routes.ManagerProjectAdd.route)
            },
            modifier = Modifier.align(Alignment.End),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text("Tambah Proyek")
        }

        Spacer(Modifier.height(16.dp))

        when (proyekList) {
            is NetworkResponse.LOADING -> {
                Text(
                    "Memuat data proyek...",
                    color = DarkGray
                )
            }
            is NetworkResponse.ERROR -> {
                Text(
                    "Terjadi kesalahan: ${(proyekList as NetworkResponse.ERROR).message}",
                    color = Color.Red
                )
            }
            is NetworkResponse.SUCCESS -> {
                val data = (proyekList as NetworkResponse.SUCCESS<ProyekProgressResponse>).data.data.proyek

                val filteredProyek = when (selectedTabIndex) {
                    0 -> data.filter { it.status == "pending" }
                    1 -> data.filter { it.status == "in-progress" }
                    2 -> data.filter { it.status == "waiting_for_review" }
                    3 -> data.filter { it.status == "done" }
                    else -> data
                }

                if (filteredProyek.isEmpty()) {
                    Text(
                        "Tidak ada proyek dengan status '$selectedStatus'",
                        color = DarkGray
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(filteredProyek) { proyek ->
                            ElevatedCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        navController.navigate(Routes.ManagerProjectDetail.managerProjectDetailWithId(proyek.id.toString()))
                                    },
                                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                                colors = CardDefaults.elevatedCardColors(
                                    containerColor = Color.White
                                )
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    // Project header with name and status
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            proyek.namaProyek,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold
                                        )

                                        // Status chip
                                        val (statusColor, textColor) = when(proyek.status.lowercase()) {
                                            "done" -> Color.Black to Color.White
                                            "in-progress" -> DarkGray to Color.White
                                            "pending" -> LightGray to Color.Black
                                            else -> LightGray to DarkGray
                                        }

                                        Surface(
                                            shape = RoundedCornerShape(16.dp),
                                            color = statusColor,
                                        ) {
                                            Text(
                                                proyek.status,
                                                style = MaterialTheme.typography.labelSmall,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                color = textColor
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Description with limited lines
                                    Text(
                                        "Deskripsi:",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = DarkGray,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        proyek.deskripsiProyek,
                                        style = MaterialTheme.typography.bodySmall,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.padding(bottom = 8.dp),
                                        color = Color.Black
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    // Progress indicator if available
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
                                                trackColor = LightGray,
                                                color = Color.Black
                                            )
                                            Text(
                                                "${proyek.progress}%",
                                                style = MaterialTheme.typography.labelSmall,
                                                modifier = Modifier.padding(start = 8.dp),
                                                color = Color.Black
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))
                                    }

                                    // Footer with deadline and other metadata
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Deadline with icon
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.CalendarToday,
                                                contentDescription = "Deadline",
                                                modifier = Modifier.size(16.dp),
                                                tint = DarkGray
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                "Deadline: ${proyek.tenggatWaktu}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = DarkGray
                                            )
                                        }

                                        // Arrow indicator for navigation
                                        Icon(
                                            imageVector = Icons.Outlined.ChevronRight,
                                            contentDescription = "View Details",
                                            tint = DarkGray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else -> Text("Belum ada data.", color = DarkGray)
        }
    }
}