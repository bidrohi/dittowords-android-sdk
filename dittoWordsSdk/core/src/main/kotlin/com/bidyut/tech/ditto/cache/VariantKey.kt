package com.bidyut.tech.ditto.cache

import com.bidyut.tech.ditto.service.json.ProjectId
import com.bidyut.tech.ditto.service.json.VariantId

internal data class VariantKey(
    val projectId: ProjectId,
    val variantId: VariantId,
)
