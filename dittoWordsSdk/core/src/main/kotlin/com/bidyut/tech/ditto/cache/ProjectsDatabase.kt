package com.bidyut.tech.ditto.cache

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.bidyut.tech.ditto.service.json.Project
import com.bidyut.tech.ditto.service.json.Variant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.toInstant
import kotlin.time.Duration

class ProjectsDatabase(
    databaseDriverFactory: DatabaseDriverFactory,
    private val cacheInterval: Duration,
) {
    private val database by lazy {
        DittoDatabase(databaseDriverFactory.createProjectsDriver())
    }
    private val dbQuery by lazy {
        database.dittoDatabaseQueries
    }
    private val dispatcher by lazy {
        Dispatchers.Unconfined
    }

    internal suspend fun insertProjects(
        projects: List<Project>,
    ) = withContext(Dispatchers.Unconfined) {
        dbQuery.transaction {
            dbQuery.removeAllProjects()
            for (project in projects) {
                dbQuery.insertProject(
                    project.id,
                    project.name,
                    Clock.System.now()
                        .toString(),
                )
            }
        }
    }

    internal fun getProjects(): Flow<List<Project>?> = dbQuery.selectProjects()
        .asFlow()
        .mapToList(dispatcher)
        .map { results ->
            val maxAge = results.firstOrNull()?.updatedAt?.toInstant()
            if (maxAge == null || Clock.System.now()
                    .minus(maxAge) >= cacheInterval
            ) {
                null
            } else {
                results.map { project ->
                    Project(
                        project.id,
                        project.name,
                    )
                }
            }
        }

    internal suspend fun insertVariants(
        variants: List<Variant>,
    ) = withContext(Dispatchers.Unconfined) {
        dbQuery.transaction {
            dbQuery.removeAllVariants()
            for (variant in variants) {
                dbQuery.insertVariant(
                    variant.apiId,
                    variant.name,
                    Clock.System.now()
                        .toString(),
                )
            }
        }
    }

    internal fun getVariants(): Flow<List<Variant>?> = dbQuery.selectVariants()
        .asFlow()
        .mapToList(dispatcher)
        .map { results ->
            val maxAge = results.firstOrNull()?.updatedAt?.toInstant()
            if (maxAge == null || Clock.System.now()
                    .minus(maxAge) >= cacheInterval
            ) {
                null
            } else {
                results.map { variant ->
                    Variant(
                        variant.id,
                        variant.name,
                    )
                }
            }
        }
}
