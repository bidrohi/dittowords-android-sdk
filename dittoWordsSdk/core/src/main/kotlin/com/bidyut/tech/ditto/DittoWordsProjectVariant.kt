package com.bidyut.tech.ditto

import com.bidyut.tech.ditto.service.json.BaseVariantId
import com.bidyut.tech.ditto.service.json.ProjectId
import com.bidyut.tech.ditto.service.json.VariantId

class DittoWordsProjectVariant(
    private val dittoWords: DittoWords,
    private val projectId: ProjectId,
    private val variantId: VariantId = BaseVariantId,
) {
    fun getStrings() = dittoWords.getStrings(
        projectId,
        variantId,
    )

    fun getStringByKey(
        key: String,
    ) = dittoWords.getStringByKey(
        projectId,
        variantId,
        key,
    )
}
