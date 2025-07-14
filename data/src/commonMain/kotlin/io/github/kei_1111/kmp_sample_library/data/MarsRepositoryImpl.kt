package io.github.kei_1111.kmp_sample_library.data

import io.github.kei_1111.kmp_sample_library.core.domain.data.MarsRepository
import io.github.kei_1111.kmp_sample_library.core.domain.network.MarsApiService
import io.github.kei_1111.kmp_sample_library.core.model.MarsProperty

class MarsRepositoryImpl(
    private val marsApi: MarsApiService,
) : MarsRepository {
    private val properties: List<MarsProperty> = emptyList()

    override suspend fun getProperties(): List<MarsProperty> {
        return properties.ifEmpty {
            marsApi.fetchProperties()
        }
    }

    private companion object {
        val TAG = "MarsRepository"
    }
}