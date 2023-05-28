package com.bidyut.tech.ditto.okhttp.network

import com.bidyut.tech.ditto.okhttp.service.DittoServiceOkHttpService
import com.bidyut.tech.ditto.service.DittoServiceFactory
import com.bidyut.tech.ditto.service.DittoWordsService
import okhttp3.OkHttpClient
import okhttp3.internal.userAgent

class DittoServiceOkHttpFactory(
    client: (OkHttpClient.Builder) -> OkHttpClient,
): DittoServiceFactory<OkHttpClient>(
    client(OkHttpClient.Builder()
        .addNetworkInterceptor {
            it.proceed(
                it.request()
                    .newBuilder()
                    .addHeader(
                        "User-Agent",
                        "DittoWordsSDK/0.0.1 $userAgent"
                    )
                    .build()
            )
        })
) {
    override fun makeService(
        apiToken: String,
    ): DittoWordsService = DittoServiceOkHttpService(
        apiToken,
        client
    )
}
