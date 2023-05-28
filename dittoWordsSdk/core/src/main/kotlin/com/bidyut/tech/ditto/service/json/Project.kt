package com.bidyut.tech.ditto.service.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias ProjectId = String

@Serializable
data class Project(
    @SerialName("id")
    val id: ProjectId,
    @SerialName("name")
    val name: String,
)
