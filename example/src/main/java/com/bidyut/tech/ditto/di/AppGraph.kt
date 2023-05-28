package com.bidyut.tech.ditto.di

import android.app.Application
import android.content.Context
import com.bidyut.tech.ditto.DittoWords
import com.bidyut.tech.ditto.example.ApiTokenStorage

abstract class AppGraph(
    private val application: Application,
) {
    val context: Context
        get() = application.applicationContext

    val tokenStorage by lazy {
        ApiTokenStorage(application.applicationContext)
    }

    abstract val dittoWords: DittoWords

    companion object {
        lateinit var instance: AppGraph
            private set

        fun assign(
            graph: AppGraph,
        ) {
            instance = graph
        }
    }
}
