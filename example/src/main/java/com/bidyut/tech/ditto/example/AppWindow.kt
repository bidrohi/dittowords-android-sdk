package com.bidyut.tech.ditto.example

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bidyut.tech.ditto.di.AppGraph
import com.bidyut.tech.ditto.ui.screen.ConfigurationScreen
import com.bidyut.tech.ditto.ui.screen.ProjectListScreen
import com.bidyut.tech.ditto.ui.screen.StringListScreen
import com.bidyut.tech.ditto.ui.theme.MyApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppWindow() {
    MyApplicationTheme {
        Scaffold(modifier = Modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(title = {
                    Text(stringResource(id = R.string.app_name))
                })
            }) { padding ->
            val navController = rememberNavController()
            val hasToken = AppGraph.instance.tokenStorage.key?.isNotEmpty() == true
            NavHost(
                navController = navController,
                startDestination = if (hasToken) {
                    Route.ProjectList.uri
                } else {
                    Route.Configure.uri
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
            ) {
                composable(
                    route = Route.Configure.uri,
                ) {
                    ConfigurationScreen(
                        navController = navController,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                    )
                }
                composable(
                    route = Route.ProjectList.uri
                ) {
                    ProjectListScreen(
                        navController = navController,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                    )
                }
                composable(
                    route = Route.StringList("{projectId}").uri,
                    arguments = listOf(
                        navArgument("projectId") { type = NavType.StringType },
                    ),
                ) {
                    val projectId = it.arguments?.getString("projectId")
                        .orEmpty()
                    StringListScreen(
                        projectId = projectId,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                    )
                }
            }
        }
    }
}
