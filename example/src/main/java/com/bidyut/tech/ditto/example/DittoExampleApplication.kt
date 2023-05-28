package com.bidyut.tech.ditto.example

import android.app.Application
import com.bidyut.tech.ditto.di.AppGraph
import com.bidyut.tech.ditto.di.ExampleAppGraph

class DittoExampleApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        AppGraph.assign(ExampleAppGraph(this))
    }
}
