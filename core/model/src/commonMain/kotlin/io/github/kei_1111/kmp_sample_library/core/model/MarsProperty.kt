package io.github.kei_1111.kmp_sample_library.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MarsProperty(
    val id: String,
    val price: Int,
    val type: PropertyType,
    @SerialName("img_src")
    val imgSrc: String
)

@Serializable
enum class PropertyType {
    @SerialName("buy")
    BUY,
    @SerialName("rent")
    RENT
}
