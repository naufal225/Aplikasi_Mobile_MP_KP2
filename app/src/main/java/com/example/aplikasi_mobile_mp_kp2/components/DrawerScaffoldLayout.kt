package com.example.aplikasi_mobile_mp_kp2.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
                drawerRoutes.forEach { route ->
                    NavigationDrawerItem(
                        label = { Text(route.label ?: route.route) },
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
                    title = { Text(title) },
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
