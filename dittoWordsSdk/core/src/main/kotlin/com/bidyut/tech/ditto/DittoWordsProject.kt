package com.bidyut.tech.ditto

import com.bidyut.tech.ditto.service.json.BaseVariantId
import com.bidyut.tech.ditto.service.json.ProjectId
import com.bidyut.tech.ditto.service.json.VariantId

class DittoWordsProject(
    private val dittoWords: DittoWords,
    private val projectId: ProjectId,
) {
    fun getStrings(
        variantId: VariantId = BaseVariantId,
    ) = dittoWords.getStrings(
        projectId,
        variantId,
    )

    fun getStringByKey(
        key: String,
        variantId: VariantId = BaseVariantId,
    ) = dittoWords.getStringByKey(
        projectId,
        variantId,
        key,
    )
}
