package com.bidyut.tech.ditto.ui.screen

import com.bidyut.tech.ditto.service.json.BaseVariantId
import com.bidyut.tech.ditto.service.json.ProjectId
import com.bidyut.tech.ditto.service.json.Variant
import com.bidyut.tech.ditto.service.json.VariantId

interface StringListContract {
    sealed interface Trigger {
        data class FetchStringList(
            val projectId: ProjectId,
            val variantId: VariantId,
        ): Trigger

        data class StringClicked(
            val key: String,
        ): Trigger

        data class VariantChanged(
            val projectId: ProjectId,
            val variantId: VariantId,
        ): Trigger
    }

    sealed interface UiState {
        data class Connecting(
            val variantId: VariantId = BaseVariantId,
        ): UiState

        data class Error(
            val message: String,
        ): UiState

        data class Strings(
            val strings: Map<String, String>,
            val variantId: VariantId = BaseVariantId,
            val variants: List<Variant> = emptyList(),
        ): UiState
    }

    sealed interface Effect {
        data class CopyKeyToClipboard(
            val key: String,
        ): Effect
    }
}
