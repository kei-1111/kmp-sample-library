package io.github.kei_1111.kmp_sample_library.core.network

import io.github.kei_1111.kmp_sample_library.core.model.MarsProperty
import io.github.kei_1111.kmp_sample_library.core.model.PropertyType
import io.github.kei_1111.kmp_sample_library.core.network.client.HttpClientFactory
import io.github.kei_1111.kmp_sample_library.core.network.core.defaultConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class MarsApiTest {

    @Test
    fun testMarsPropertyDeserialization() = runTest {
        val mockEngine = MockEngine { request ->
            respond(
                content = """
                    [
                        {
                            "id": "424906",
                            "price": 8000000,
                            "type": "BUY",
                            "img_src": "https://i.imgur.com/gohQ0Ep.jpg"
                        },
                        {
                            "id": "424907",
                            "price": 2000,
                            "type": "RENT",
                            "img_src": "https://i.imgur.com/exDRZMb.jpg"
                        }
                    ]
                """.trimIndent(),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = HttpClient(mockEngine) {
            defaultConfig()
        }

        val properties = client.get("https://android-kotlin-fun-mars-server.appspot.com/photos").body<List<MarsProperty>>()
        
        assertEquals(2, properties.size)
        
        val firstProperty = properties[0]
        assertEquals("424906", firstProperty.id)
        assertEquals(8000000, firstProperty.price)
        assertEquals(PropertyType.BUY, firstProperty.type)
        assertEquals("https://i.imgur.com/gohQ0Ep.jpg", firstProperty.imgSrc)
        
        val secondProperty = properties[1]
        assertEquals("424907", secondProperty.id)
        assertEquals(2000, secondProperty.price)
        assertEquals(PropertyType.RENT, secondProperty.type)
        assertEquals("https://i.imgur.com/exDRZMb.jpg", secondProperty.imgSrc)

        client.close()
    }

    @Test
    fun testRealMarsApiCall() = runTest {
        val client = HttpClientFactory.create()
        
        try {
            val properties = client.get("https://android-kotlin-fun-mars-server.appspot.com/photos").body<List<MarsProperty>>()
            assertNotNull(properties)
            println("Successfully fetched ${properties.size} Mars properties")
            
            if (properties.isNotEmpty()) {
                val firstProperty = properties.first()
                assertNotNull(firstProperty.id)
                assertNotNull(firstProperty.imgSrc)
                println("First property: id=${firstProperty.id}, type=${firstProperty.type}, price=${firstProperty.price}")
            }
        } catch (e: Exception) {
            println("Warning: Real API test failed (this might be expected in CI): ${e.message}")
        } finally {
            client.close()
        }
    }
}