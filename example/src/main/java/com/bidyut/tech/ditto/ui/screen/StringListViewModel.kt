package com.bidyut.tech.ditto.ui.screen

import androidx.lifecycle.viewModelScope
import com.bidyut.tech.ditto.di.AppGraph
import com.bidyut.tech.ditto.service.json.ProjectId
import com.bidyut.tech.ditto.service.json.VariantId
import com.bidyut.tech.ditto.ui.TriggerEffectViewModel
import kotlinx.coroutines.launch

class StringListViewModel: TriggerEffectViewModel<
        StringListContract.Trigger,
        StringListContract.UiState,
        StringListContract.Effect,
        >() {
    override fun makeInitialState(): StringListContract.UiState = StringListContract.UiState.Connecting()

    override fun handleTriggers(
        trigger: StringListContract.Trigger,
    ) {
        when (trigger) {
            is StringListContract.Trigger.FetchStringList -> fetchStringsList(
                trigger.projectId,
                trigger.variantId
            )

            is StringListContract.Trigger.StringClicked -> copyKeyToClipboard(trigger.key)
            is StringListContract.Trigger.VariantChanged -> fetchStringsList(
                trigger.projectId,
                trigger.variantId
            )
        }
    }

    private fun fetchStringsList(
        projectId: ProjectId,
        variantId: VariantId,
    ) {
        val dittoWords = AppGraph.instance.dittoWords
        viewModelScope.launch {
            dittoWords.getStrings(
                projectId,
                variantId
            )
                .collect {
                    if (it.isSuccess) {
                        setState {
                            if (this is StringListContract.UiState.Strings) {
                                StringListContract.UiState.Strings(
                                    it.getOrNull()
                                        .orEmpty(),
                                    variantId,
                                    variants,
                                )
                            } else {
                                StringListContract.UiState.Strings(
                                    it.getOrNull()
                                        .orEmpty(),
                                    variantId,
                                    emptyList(),
                                )
                            }
                        }
                    } else {
                        setState {
                            StringListContract.UiState.Error(it.exceptionOrNull()?.message.orEmpty())
                        }
                    }
                }
        }
        viewModelScope.launch {
            dittoWords.getVariants()
                .collect {
                    if (it.isSuccess) {
                        setState {
                            val newVariants = it.getOrNull()
                                .orEmpty()
                            if (this is StringListContract.UiState.Strings) {
                                StringListContract.UiState.Strings(
                                    strings,
                                    variantId,
                                    newVariants,
                                )
                            } else {
                                StringListContract.UiState.Strings(
                                    emptyMap(),
                                    variantId,
                                    newVariants,
                                )
                            }
                        }
                    } else {
                        setState {
                            StringListContract.UiState.Error(it.exceptionOrNull()?.message.orEmpty())
                        }
                    }
                }
        }
    }

    private fun copyKeyToClipboard(
        key: String,
    ) {
        sendEffect {
            StringListContract.Effect.CopyKeyToClipboard(key)
        }
    }
}
