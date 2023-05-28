package com.bidyut.tech.ditto.data

import com.bidyut.tech.ditto.cache.StringsDatabase
import com.bidyut.tech.ditto.service.DittoWordsService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StringsRepository(
    private val service: DittoWordsService,
    private val database: StringsDatabase,
) {
    fun getStrings(): Flow<Result<Map<String, String>>> {
        return database.getStrings()
            .map {
                if (it != null) {
                    Result.success(it)
                } else {
                    fetchStrings()
                }
            }
    }

    private suspend fun fetchStrings(): Result<Map<String, String>> = service.getProjectStrings(
        database.projectId,
        database.variantId
    )
        .onSuccess { database.insertStrings(it) }

    fun getStringByKey(
        key: String,
    ): Flow<Result<String>> = database.getStringByKey(key)
        .map {
            fetchStrings().map {
                it[key].orEmpty()
            }
        }
}
