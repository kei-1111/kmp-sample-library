package io.github.kei_1111.kmp_sample_library.core.network.client

import io.github.kei_1111.kmp_sample_library.core.network.core.defaultConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin

actual object HttpClientFactory {
    actual fun create() = HttpClient(Darwin) {
        defaultConfig()
    }
}