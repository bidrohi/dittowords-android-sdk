package com.bidyut.tech.ditto.cache

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.bidyut.tech.ditto.service.json.ProjectId
import com.bidyut.tech.ditto.service.json.VariantId

class DatabaseDriverFactory(
    private val context: Context,
) {
    fun createProjectsDriver(): SqlDriver {
        return AndroidSqliteDriver(
            DittoDatabase.Schema,
            context,
            "projects.db"
        )
    }

    fun createStringsDriver(
        projectId: ProjectId,
        variantId: VariantId,
    ): SqlDriver {
        return AndroidSqliteDriver(
            WordsDatabase.Schema,
            context,
            "$projectId-$variantId.db"
        )
    }
}
