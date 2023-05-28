package com.bidyut.tech.ditto.di

import android.app.Application
import android.util.Log
import com.bidyut.tech.ditto.DittoWords
import com.bidyut.tech.ditto.cache.DatabaseDriverFactory
import com.bidyut.tech.ditto.example.BuildConfig
import com.bidyut.tech.ditto.ktor.service.DittoServiceKtorFactory
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class ExampleAppGraph(
    application: Application,
): AppGraph(application) {
    override val dittoWords by lazy {
        DittoWords {
            apiToken = tokenStorage.key!!
            dittoServiceFactory = DittoServiceKtorFactory(Android) {
                if (BuildConfig.DEBUG) {
                    install(Logging) {
                        logger = object: Logger {
                            override fun log(message: String) {
                                Log.d(
                                    "HTTP Client",
                                    message
                                )
                            }
                        }
                        level = LogLevel.INFO
                    }
                }
                install(HttpCache)
            }
            databaseDriverFactory = DatabaseDriverFactory(context)
            cacheInterval = 15.toDuration(DurationUnit.MINUTES)
        }
    }
}
