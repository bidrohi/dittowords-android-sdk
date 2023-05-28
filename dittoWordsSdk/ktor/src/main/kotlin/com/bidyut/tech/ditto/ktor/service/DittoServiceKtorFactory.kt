package com.bidyut.tech.ditto.ktor.service

import com.bidyut.tech.ditto.service.DittoServiceFactory
import com.bidyut.tech.ditto.service.DittoWordsService
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class DittoServiceKtorFactory(
    engineFactory: HttpClientEngineFactory<*>,
    client: HttpClientConfig<*>.() -> Unit,
): DittoServiceFactory<HttpClient>(HttpClient(engineFactory) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
    install(ContentEncoding) {
        deflate(1.0F)
        gzip(0.9F)
    }
    install(UserAgent) {
        agent = "DittoWordsSDK/0.0.1"
    }
    client()
}) {
    override fun makeService(
        apiToken: String,
    ): DittoWordsService = DittoWordsKtorService(
        apiToken,
        client,
    )
}
