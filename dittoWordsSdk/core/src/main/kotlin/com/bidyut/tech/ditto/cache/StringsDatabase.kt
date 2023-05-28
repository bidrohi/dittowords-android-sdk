package com.bidyut.tech.ditto.cache

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneNotNull
import com.bidyut.tech.ditto.service.json.ProjectId
import com.bidyut.tech.ditto.service.json.VariantId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant
import kotlin.time.Duration

class StringsDatabase(
    databaseDriverFactory: DatabaseDriverFactory,
    val projectId: ProjectId,
    val variantId: VariantId,
    private val cacheInterval: Duration,
) {
    private val database by lazy {
        WordsDatabase(
            databaseDriverFactory.createStringsDriver(
                projectId,
                variantId
            )
        )
    }
    private val dbQuery by lazy {
        database.wordsDatabaseQueries
    }
    private val dispatcher by lazy {
        Dispatchers.Unconfined
    }

    internal suspend fun insertStrings(
        strings: Map<String, String>,
    ) = withContext(Dispatchers.Unconfined) {
        dbQuery.transaction {
            dbQuery.removeAllStrings()
            for ((key, value) in strings) {
                dbQuery.insertString(
                    key,
                    value,
                )
            }
            dbQuery.insertStringUpdate(
                projectId,
                variantId,
                Clock.System.now()
                    .toString()
            )
        }
    }

    internal fun getStrings(): Flow<Map<String, String>?> = getMaxUpdatedAt().combine(dbQuery.selectStrings()
        .asFlow()
        .mapToList(dispatcher)
        .map {
            it.associateBy(
                { it.key },
                { it.stringValue },
            )
        }) { maxUpdatedAt, strings ->
        if (maxUpdatedAt == null || Clock.System.now()
                .minus(maxUpdatedAt) >= cacheInterval
        ) {
            null
        } else {
            strings
        }
    }

    internal fun getStringByKey(
        key: String,
    ): Flow<String> = dbQuery.selectStringsByKey(key)
        .asFlow()
        .mapToOneNotNull(dispatcher)
        .map {
            it.stringValue
        }

    private fun getMaxUpdatedAt(): Flow<Instant?> = dbQuery.selectUpdatedAt()
        .asFlow()
        .mapToList(dispatcher)
        .map { results ->
            results.first().maxUpdatedAt?.toInstant()
        }
}
