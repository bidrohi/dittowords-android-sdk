package com.bidyut.tech.ditto.ui.screen

import com.bidyut.tech.ditto.service.json.Project
import com.bidyut.tech.ditto.service.json.ProjectId

interface ProjectListContract {
    sealed interface Trigger {
        object FetchProjectList: Trigger

        data class ProjectClicked(
            val project: Project,
        ): Trigger
    }

    sealed interface UiState {
        object Connecting: UiState

        data class Error(
            val message: String,
        ): UiState

        data class Projects(
            val projects: List<Project>,
        ): UiState
    }

    sealed interface Effect {
        data class NavigateToStrings(
            val projectId: ProjectId,
        ): Effect
    }
}
