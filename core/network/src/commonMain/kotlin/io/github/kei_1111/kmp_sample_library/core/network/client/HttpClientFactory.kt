package io.github.kei_1111.kmp_sample_library.core.network.client

import io.ktor.client.HttpClient

expect object HttpClientFactory {
    fun create(): HttpClient
}