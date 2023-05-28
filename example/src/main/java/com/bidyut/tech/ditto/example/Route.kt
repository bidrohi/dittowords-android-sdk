package com.bidyut.tech.ditto.example

import androidx.navigation.NavController
import com.bidyut.tech.ditto.service.json.ProjectId

sealed class Route(
    val uri: String,
) {
    object Configure: Route("configure")

    object ProjectList: Route("projects")

    data class StringList(
        val projectId: ProjectId,
    ): Route("strings/$projectId")

    fun navigate(
        navController: NavController,
    ) {
        navController.navigate(uri)
    }
}
