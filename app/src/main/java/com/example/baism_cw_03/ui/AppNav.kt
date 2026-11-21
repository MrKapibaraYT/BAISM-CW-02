package com.example.baism_cw_03.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch


object Routes {
    const val HOME = "home"
    const val ADD = "add"
    const val LIST = "list"
    const val DELETE = "delete"
}

private data class Tab(val route: String, val label: String, val icon: ImageVector)

private val tabs = listOf(
    Tab(
        route = Routes.ADD,
        "Dodaj osobę",
        icon = Icons.Filled.Add
    ),
    Tab(
        route = Routes.LIST,
        "Lista danych",
        icon = Icons.AutoMirrored.Filled.List
    ),
    Tab(
        route = Routes.DELETE,
        "Usuń osobę",
        icon = Icons.Filled.Delete
    )
)

private fun titleFor(route: String) = when(route) {
    Routes.ADD -> "Dodaj osobę"
    Routes.LIST -> "Lista osób"
    Routes.DELETE -> "Usuń osobę"
    else -> "Dane osobowe"
}

private fun navigateToTab(navController: NavHostController, route: String) {
    navController.navigate(route) {
        popUpTo(Routes.HOME)
        launchSingleTop = true
    }
}

@Composable
fun AppNav(viewModel: PersonViewModel) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: Routes.HOME
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val people by viewModel.people.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titleFor(currentRoute)) },
                navigationIcon = {
                    if (currentRoute != Routes.HOME) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Wstecz")
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                tabs.forEach { tab ->
                    NavigationBarItem(
                        selected = currentRoute == tab.route,
                        onClick = { navigateToTab(navController, tab.route) },
                        icon = { Icon(tab.icon, contentDescription = null)},
                        label = { Text(tab.label) },
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(padding),
        ) {
            composable(Routes.HOME) {
                HomeScreen(onNavigate = { route -> navController.navigate(route)})
            }
            composable(Routes.ADD) {
                AddPersonScreen(
                    onSubmit = { form -> viewModel.addPerson(form)},
                    onSaved = { scope.launch { snackbarHostState.showSnackbar("Osoba ta została zapisana") }}
                )
            }
            composable(Routes.LIST) { PersonListScreen(people) }
            composable(Routes.DELETE) {
                DeletePersonScreen(
                    people = people,
                    onDelete = { person ->
                        viewModel.deletePerson(person.id)
                        scope.launch { snackbarHostState.showSnackbar("Osoba została usunięta") }
                    }
                )
            }
        }
    }
}