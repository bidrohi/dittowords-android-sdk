package com.bidyut.tech.ditto.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.bidyut.tech.ditto.example.Route
import com.bidyut.tech.ditto.service.json.Project
import kotlinx.coroutines.launch

@Composable
fun ProjectListScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: ProjectListViewModel = viewModel(),
) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(true) {
        scope.launch {
            viewModel.effect.collect {
                when (it) {
                    is ProjectListContract.Effect.NavigateToStrings -> {
                        Route.StringList(it.projectId)
                            .navigate(navController)
                    }
                }
            }
        }
    }
    val state = viewModel.viewState.collectAsState().value
    when (state) {
        ProjectListContract.UiState.Connecting -> {
            CircularProgressIndicator()
            viewModel.sendTrigger(ProjectListContract.Trigger.FetchProjectList)
        }

        is ProjectListContract.UiState.Error -> {
            Text(text = state.message)
        }

        is ProjectListContract.UiState.Projects -> {
            ProjectListView(
                projects = state.projects,
                onItemClick = {
                    viewModel.sendTrigger(ProjectListContract.Trigger.ProjectClicked(it))
                },
                modifier = modifier,
            )
        }
    }
}

@Composable
fun ProjectListView(
    projects: List<Project>,
    onItemClick: (Project) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(4.dp),
    ) {
        items(projects) {
            ProjectCard(project = it,
                modifier = Modifier.clickable {
                    onItemClick(it)
                })
            Divider()
        }
    }
}

@Composable
fun ProjectCard(
    project: Project,
    modifier: Modifier = Modifier,
) {
    ListItem(
        leadingContent = {
            Image(
                imageVector = Icons.TwoTone.ArrowForward,
                contentDescription = project.name,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .padding(4.dp)
                    .size(32.dp)
            )
        },
        headlineContent = { Text(project.name) },
        modifier = modifier,
    )
}
