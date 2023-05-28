package com.bidyut.tech.ditto.service.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias VariantId = String

const val BaseVariantId: VariantId = "--base--"

@Serializable
data class Variant(
    @SerialName("apiID")
    val apiId: VariantId,
    @SerialName("name")
    val name: String,
) {
    val displayName: String
        get() = name.ifEmpty { apiId }
}
