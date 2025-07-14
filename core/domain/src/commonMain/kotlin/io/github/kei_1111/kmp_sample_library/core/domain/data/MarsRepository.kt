package io.github.kei_1111.kmp_sample_library.core.domain.data

import io.github.kei_1111.kmp_sample_library.core.model.MarsProperty

interface MarsRepository {
    suspend fun getProperties(): List<MarsProperty>
}