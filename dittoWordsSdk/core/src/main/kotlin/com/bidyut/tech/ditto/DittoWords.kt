package com.bidyut.tech.ditto

import com.bidyut.tech.ditto.cache.ProjectsDatabase
import com.bidyut.tech.ditto.cache.StringsDatabase
import com.bidyut.tech.ditto.cache.VariantKey
import com.bidyut.tech.ditto.data.ProjectRepository
import com.bidyut.tech.ditto.data.StringsRepository
import com.bidyut.tech.ditto.service.json.BaseVariantId
import com.bidyut.tech.ditto.service.json.ProjectId
import com.bidyut.tech.ditto.service.json.Variant
import com.bidyut.tech.ditto.service.json.VariantId
import kotlinx.coroutines.flow.map

class DittoWords(
    configBuilder: DittoWordsConfig.Builder.() -> Unit,
) {
    private val config: DittoWordsConfig = DittoWordsConfig.Builder()
        .apply {
            configBuilder()
        }
        .build()

    private val dittoWordsService by lazy {
        config.dittoServiceFactory.makeService(
            config.apiToken
        )
    }

    private val projectsDatabase by lazy {
        ProjectsDatabase(
            config.databaseDriverFactory,
            config.cacheInterval
        )
    }

    private val projectRepository by lazy {
        ProjectRepository(
            dittoWordsService,
            projectsDatabase
        )
    }

    private val stringsRepositories: Map<VariantKey, StringsRepository> = mutableMapOf()

    private fun getStringsRepository(
        projectId: ProjectId,
        variantId: VariantId,
    ): StringsRepository {
        return stringsRepositories.withDefault {
            StringsRepository(
                dittoWordsService,
                StringsDatabase(
                    config.databaseDriverFactory,
                    it.projectId,
                    it.variantId,
                    config.cacheInterval,
                ),
            )
        }
            .getValue(
                VariantKey(
                    projectId,
                    variantId
                )
            )
    }

    fun getProjects() = projectRepository.getProjects()

    private val baseVariant = Variant(
        BaseVariantId,
        "Base"
    )

    fun getVariants() = projectRepository.getVariants()
        .map {
            if (it.isSuccess) {
                Result.success(
                    listOf(baseVariant) + it.getOrNull()
                        .orEmpty()
                )
            } else {
                it
            }
        }

    fun getStrings(
        projectId: ProjectId,
        variantId: VariantId = BaseVariantId,
    ) = getStringsRepository(
        projectId,
        variantId
    ).getStrings()

    fun getStringByKey(
        key: String,
        projectId: ProjectId,
        variantId: VariantId = BaseVariantId,
    ) = getStringsRepository(
        projectId,
        variantId
    ).getStringByKey(key)
}
