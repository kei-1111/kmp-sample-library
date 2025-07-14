package io.github.kei_1111.kmp_sample_library.core.domain.network

import io.github.kei_1111.kmp_sample_library.core.model.MarsProperty

interface MarsApiService {
    suspend fun fetchProperties(): List<MarsProperty>
}