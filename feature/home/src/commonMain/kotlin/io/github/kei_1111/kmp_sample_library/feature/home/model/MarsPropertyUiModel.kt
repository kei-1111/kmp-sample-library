package io.github.kei_1111.kmp_sample_library.feature.home.model

import io.github.kei_1111.kmp_sample_library.core.model.MarsProperty
import io.github.kei_1111.kmp_sample_library.core.model.PropertyType

data class MarsPropertyUiModel(
    val id: String,
    val price: String,
    val type: String,
    val imageUrl: String,
) {
    companion object {
        fun convert(marsProperty: MarsProperty): MarsPropertyUiModel {
            val reversedPrice = marsProperty.price.toString().reversed()
            val formattedPrice = reversedPrice.chunked(3).joinToString(",").reversed()

            return MarsPropertyUiModel(
                id = marsProperty.id,
                price = "$$formattedPrice",
                type = marsProperty.type.name,
                imageUrl = marsProperty.imgSrc,
            )
        }
    }
}
