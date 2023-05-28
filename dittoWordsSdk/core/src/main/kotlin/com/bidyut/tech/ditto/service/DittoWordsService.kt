package com.bidyut.tech.ditto.service

import com.bidyut.tech.ditto.service.json.BaseVariantId
import com.bidyut.tech.ditto.service.json.Project
import com.bidyut.tech.ditto.service.json.ProjectId
import com.bidyut.tech.ditto.service.json.Variant
import com.bidyut.tech.ditto.service.json.VariantId

abstract class DittoWordsService(
    protected val apiToken: String,
) {
    abstract suspend fun getVariants(): Result<List<Variant>>

    abstract suspend fun getProjects(): Result<List<Project>>

    abstract suspend fun getProjectStrings(
        projectId: ProjectId,
        variantId: VariantId = BaseVariantId,
    ): Result<Map<String, String>>

    companion object {
        const val baseUrl = "https://api.dittowords.com"
    }
}
