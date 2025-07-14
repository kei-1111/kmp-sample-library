package io.github.kei_1111.kmp_sample_library.core.network.api

import io.github.kei_1111.kmp_sample_library.core.domain.network.MarsApiService
import io.github.kei_1111.kmp_sample_library.core.model.MarsProperty
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class MarsApiServiceImpl(
    private val client: HttpClient,
    private val baseUrl: String = "https://android-kotlin-fun-mars-server.appspot.com"
) : MarsApiService {
    override suspend fun fetchProperties(): List<MarsProperty> =
        client.get("$baseUrl/realestate").body()
}