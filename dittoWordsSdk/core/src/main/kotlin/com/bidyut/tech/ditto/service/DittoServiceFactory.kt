package com.bidyut.tech.ditto.service

abstract class DittoServiceFactory<C>(
    protected val client: C,
) {
    abstract fun makeService(
        apiToken: String,
    ): DittoWordsService
}
