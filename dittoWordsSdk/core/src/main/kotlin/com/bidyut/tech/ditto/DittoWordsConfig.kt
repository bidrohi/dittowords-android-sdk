package com.bidyut.tech.ditto

import com.bidyut.tech.ditto.cache.DatabaseDriverFactory
import com.bidyut.tech.ditto.service.DittoServiceFactory
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class DittoWordsConfig(
    internal val apiToken: String,
    internal val cacheInterval: Duration,
    internal val dittoServiceFactory: DittoServiceFactory<*>,
    internal val databaseDriverFactory: DatabaseDriverFactory,
) {
    class Builder {
        var apiToken: String? = null
        var cacheInterval: Duration = 30.toDuration(DurationUnit.MINUTES)
        var dittoServiceFactory: DittoServiceFactory<*>? = null
        var databaseDriverFactory: DatabaseDriverFactory? = null

        fun build(): DittoWordsConfig = apiToken?.let {
            DittoWordsConfig(
                it,
                cacheInterval,
                dittoServiceFactory!!,
                databaseDriverFactory!!
            )
        } ?: throw IllegalArgumentException("Missing api token")
    }
}
