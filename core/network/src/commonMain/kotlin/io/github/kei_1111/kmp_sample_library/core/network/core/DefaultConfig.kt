package io.github.kei_1111.kmp_sample_library.core.network.core

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun HttpClientConfig<*>.defaultConfig() {
    install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
    expectSuccess = true
}
