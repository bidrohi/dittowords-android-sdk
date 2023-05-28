package com.bidyut.tech.ditto.data

import com.bidyut.tech.ditto.cache.ProjectsDatabase
import com.bidyut.tech.ditto.service.DittoWordsService
import com.bidyut.tech.ditto.service.json.Project
import com.bidyut.tech.ditto.service.json.Variant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProjectRepository(
    private val service: DittoWordsService,
    private val database: ProjectsDatabase,
) {
    fun getProjects(): Flow<Result<List<Project>>> = database.getProjects()
        .map {
            if (it != null) {
                Result.success(it)
            } else {
                fetchProjects()
            }
        }

    private suspend fun fetchProjects(): Result<List<Project>> = service.getProjects()
        .onSuccess { database.insertProjects(it) }

    fun getVariants(): Flow<Result<List<Variant>>> = database.getVariants()
        .map {
            if (it != null) {
                Result.success(it)
            } else {
                fetchVariants()
            }
        }

    private suspend fun fetchVariants(): Result<List<Variant>> = service.getVariants()
        .onSuccess { database.insertVariants(it) }
}
