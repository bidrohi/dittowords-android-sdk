package com.bidyut.tech.ditto.di

import android.app.Application
import android.content.Context
import com.bidyut.tech.ditto.DittoWords
import com.bidyut.tech.ditto.DittoWordsConfig
import com.bidyut.tech.ditto.cache.DatabaseDriverFactory
import com.bidyut.tech.ditto.example.BuildConfig
import com.bidyut.tech.ditto.okhttp.network.DittoServiceOkHttpFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.internal.userAgent
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class ExampleAppGraph(
    application: Application,
): AppGraph(application) {
    override val dittoWords by lazy {
        DittoWords {
            apiToken = tokenStorage.key!!
            dittoServiceFactory = DittoServiceOkHttpFactory {
                it.cache(
                    Cache(
                        File(
                            context.cacheDir,
                            "ditto-cache"
                        ),
                        1024L * 1024L
                    )
                )
                if (BuildConfig.DEBUG) {
                    it.addInterceptor(HttpLoggingInterceptor())
                }
                it.build()
            }
            databaseDriverFactory = DatabaseDriverFactory(context)
            cacheInterval = 15.toDuration(DurationUnit.MINUTES)
        }
    }
}
