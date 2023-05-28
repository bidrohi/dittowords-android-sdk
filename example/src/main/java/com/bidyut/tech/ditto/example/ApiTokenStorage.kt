package com.bidyut.tech.ditto.example

import android.content.Context
import androidx.core.content.edit

class ApiTokenStorage(
    private val context: Context,
) {
    private val sharedPreferences by lazy {
        context.getSharedPreferences(
            "api_storage",
            Context.MODE_PRIVATE
        )
    }

    var key: String?
        get() = sharedPreferences.getString(
            TOKEN_KEY,
            null
        )
        set(value) = sharedPreferences.edit {
            putString(
                TOKEN_KEY,
                value
            )
        }

    companion object {
        private const val TOKEN_KEY = "dittoWordsToken"
    }
}
