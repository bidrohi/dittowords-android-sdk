package com.bidyut.tech.ditto.ktor.service

import com.bidyut.tech.ditto.service.DittoWordsService
import com.bidyut.tech.ditto.service.json.BaseVariantId
import com.bidyut.tech.ditto.service.json.Project
import com.bidyut.tech.ditto.service.json.ProjectId
import com.bidyut.tech.ditto.service.json.Variant
import com.bidyut.tech.ditto.service.json.VariantId
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.appendPathSegments
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DittoWordsKtorService(
    apiToken: String,
    private val client: HttpClient,
): DittoWordsService(apiToken) {
    override suspend fun getVariants(): Result<List<Variant>> = withContext(Dispatchers.Unconfined) {
        val response = client.get(baseUrl) {
            headers {
                append(
                    HttpHeaders.Authorization,
                    "token $apiToken"
                )
            }
            url {
                appendPathSegments("variants")
            }
        }
        when (response.status) {
            HttpStatusCode.OK -> Result.success(response.body())
            else -> Result.failure(IllegalStateException(response.body() as String))
        }
    }

    override suspend fun getProjects(): Result<List<Project>> = withContext(Dispatchers.Unconfined) {
        val response = client.get(baseUrl) {
            headers {
                append(
                    HttpHeaders.Authorization,
                    "token $apiToken"
                )
            }
            url {
                appendPathSegments(
                    "v1",
                    "projects"
                )
            }
        }
        when (response.status) {
            HttpStatusCode.OK -> Result.success(response.body())
            else -> Result.failure(IllegalStateException(response.body() as String))
        }
    }

    override suspend fun getProjectStrings(
        projectId: ProjectId,
        variantId: VariantId,
    ): Result<Map<String, String>> = withContext(Dispatchers.Unconfined) {
        val response = client.get(baseUrl) {
            headers {
                append(
                    HttpHeaders.Authorization,
                    "token $apiToken"
                )
            }
            url {
                appendPathSegments(
                    "v1",
                    "projects",
                    projectId
                )
                if (variantId != BaseVariantId) {
                    parameters.append(
                        "variant",
                        variantId
                    )
                }
            }
        }
        when (response.status) {
            HttpStatusCode.OK -> Result.success(response.body())
            else -> Result.failure(IllegalStateException(response.body() as String))
        }
    }
}
