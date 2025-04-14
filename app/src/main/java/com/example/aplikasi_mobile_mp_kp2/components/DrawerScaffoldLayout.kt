package com.example.aplikasi_mobile_mp_kp2.components

import android.provider.CalendarContract.Colors
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.aplikasi_mobile_mp_kp2.R
import kotlinx.coroutines.launch
import com.example.aplikasi_mobile_mp_kp2.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerScaffoldLayout(
    drawerRoutes: List<Routes>,
    navController: NavHostController,
    title: String = "MP KP 2", // bisa disesuaikan
    content: @Composable (Modifier) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Image(
                    painter = painterResource(id = R.drawable.logo_grafit), // Sesuaikan dengan ID gambar Anda
                    contentDescription = "Custom Image",
                    modifier = Modifier
                        .fillMaxWidth() // Menyesuaikan lebar gambar
                        .padding(bottom = 16.dp) // Memberikan jarak bawah untuk elemen berikutnya
                )
                drawerRoutes.forEach { route ->
                    NavigationDrawerItem(
                        shape = RoundedCornerShape(0),
                        colors = NavigationDrawerItemDefaults.colors(Color.Black),
                        label = {
                            Row {
                                Icon(
                                    imageVector = (route.icon ?: Icons.Default.Home),
                                    contentDescription = route.label,
                                    tint = if (currentRoute == route.route) Color.White else Color.Black,
                                )
                                Text(route.label ?: route.route, modifier = Modifier.padding(horizontal = 18.dp))
                            }
                        },
                        selected = currentRoute == route.route,
                        onClick = {
                            navController.navigate(route.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Image(
                            painter = painterResource(R.drawable.logo_grafit),
                            contentDescription = ""
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { innerPadding ->
            content(Modifier.padding(innerPadding))
        }
    }
}
