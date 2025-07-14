package io.github.kei_1111.kmp_sample_library.core.network

import io.github.kei_1111.kmp_sample_library.core.network.client.HttpClientFactory
import io.github.kei_1111.kmp_sample_library.core.network.core.defaultConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.headersOf
import io.ktor.client.HttpClient
import io.ktor.client.plugins.pluginOrNull
import io.ktor.client.request.get
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class HttpClientFactoryTest {

    @Test
    fun testHttpClientCreation() {
        val client = HttpClientFactory.create()
        assertNotNull(client)
        client.close()
    }

    @Test
    fun testHttpClientHasContentNegotiation() {
        val client = HttpClientFactory.create()
        assertTrue(client.pluginOrNull(ContentNegotiation) != null)
        client.close()
    }

    @Test
    fun testHttpClientWithMockEngine() = runTest {
        val mockEngine = MockEngine { request ->
            respond(
                content = """{"message": "Hello, World!"}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = HttpClient(mockEngine) {
            defaultConfig()
        }

        val response = client.get("https://api.example.com/test")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("""{"message": "Hello, World!"}""", response.bodyAsText())

        client.close()
    }
}