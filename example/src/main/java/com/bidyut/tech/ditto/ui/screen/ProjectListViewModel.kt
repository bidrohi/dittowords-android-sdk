package com.bidyut.tech.ditto.ui.screen

import androidx.lifecycle.viewModelScope
import com.bidyut.tech.ditto.di.AppGraph
import com.bidyut.tech.ditto.ui.TriggerEffectViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProjectListViewModel: TriggerEffectViewModel<
        ProjectListContract.Trigger,
        ProjectListContract.UiState,
        ProjectListContract.Effect,
        >() {
    override fun makeInitialState(): ProjectListContract.UiState = ProjectListContract.UiState.Connecting

    override fun handleTriggers(
        trigger: ProjectListContract.Trigger,
    ) {
        when (trigger) {
            ProjectListContract.Trigger.FetchProjectList -> fetchProjectList()

            is ProjectListContract.Trigger.ProjectClicked -> {
                sendEffect {
                    ProjectListContract.Effect.NavigateToStrings(
                        trigger.project.id
                    )
                }
            }
        }
    }

    private fun fetchProjectList() {
        val dittoWords = AppGraph.instance.dittoWords
        viewModelScope.launch {
            dittoWords.getProjects()
                .collect {
                    if (it.isSuccess) {
                        setState {
                            ProjectListContract.UiState.Projects(
                                it.getOrNull()
                                    .orEmpty()
                            )
                        }
                    } else {
                        setState {
                            ProjectListContract.UiState.Error(it.exceptionOrNull()?.message.orEmpty())
                        }
                    }
                }
        }
        viewModelScope.launch {
            dittoWords.getVariants()
                .collectLatest {
                    // Prefetch variants
                }
        }
    }
}
