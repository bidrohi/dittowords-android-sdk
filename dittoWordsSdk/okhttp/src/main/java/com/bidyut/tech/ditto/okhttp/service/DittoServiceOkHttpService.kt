package com.bidyut.tech.ditto.okhttp.service

import com.bidyut.tech.ditto.service.DittoWordsService
import com.bidyut.tech.ditto.service.json.BaseVariantId
import com.bidyut.tech.ditto.service.json.Project
import com.bidyut.tech.ditto.service.json.ProjectId
import com.bidyut.tech.ditto.service.json.Variant
import com.bidyut.tech.ditto.service.json.VariantId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException

@OptIn(ExperimentalSerializationApi::class)
class DittoServiceOkHttpService(
    apiToken: String,
    private val client: OkHttpClient,
): DittoWordsService(apiToken) {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override suspend fun getVariants(): Result<List<Variant>> = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .addHeader(
                "Authorization",
                "token $apiToken"
            )
            .url(
                baseUrl.toHttpUrl()
                    .newBuilder()
                    .addPathSegment("variants")
                    .build()
            )
            .build()
        val call = client.newCall(request)
        try {
            val response = call.execute()
            if (response.isSuccessful && response.body != null) {
                Result.success(json.decodeFromStream(response.body!!.byteStream()))
            } else {
                Result.failure(IllegalStateException(response.message))
            }
        } catch (ex: IOException) {
            Result.failure(ex)
        }
    }

    override suspend fun getProjects(): Result<List<Project>> = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .addHeader(
                "Authorization",
                "token $apiToken"
            )
            .url(
                baseUrl.toHttpUrl()
                    .newBuilder()
                    .addPathSegment("v1")
                    .addPathSegment("projects")
                    .build()
            )
            .build()
        val call = client.newCall(request)
        try {
            val response = call.execute()
            if (response.isSuccessful && response.body != null) {
                Result.success(json.decodeFromStream(response.body!!.byteStream()))
            } else {
                Result.failure(IllegalStateException(response.message))
            }
        } catch (ex: IOException) {
            Result.failure(ex)
        }
    }

    override suspend fun getProjectStrings(
        projectId: ProjectId,
        variantId: VariantId,
    ): Result<Map<String, String>> = withContext(Dispatchers.IO) {
        val urlBuilder = baseUrl.toHttpUrl()
            .newBuilder()
            .addPathSegment("v1")
            .addPathSegment("projects")
            .addPathSegment(projectId)
        if (variantId != BaseVariantId) {
            urlBuilder.addQueryParameter(
                "variant",
                variantId
            )
        }
        val request = Request.Builder()
            .addHeader(
                "Authorization",
                "token $apiToken"
            )
            .url(urlBuilder.build())
            .build()
        val call = client.newCall(request)
        try {
            val response = call.execute()
            if (response.isSuccessful && response.body != null) {
                Result.success(json.decodeFromStream(response.body!!.byteStream()))
            } else {
                Result.failure(IllegalStateException(response.message))
            }
        } catch (ex: IOException) {
            Result.failure(ex)
        }
    }
}
