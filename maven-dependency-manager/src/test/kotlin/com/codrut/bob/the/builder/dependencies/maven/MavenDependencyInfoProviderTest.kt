package com.codrut.bob.the.builder.dependencies.maven

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.tests.utils.config
import io.ktor.client.tests.utils.test
import io.ktor.client.tests.utils.testWithEngine
import io.ktor.serialization.kotlinx.xml.xml
import java.io.File
import kotlin.test.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MavenDependencyInfoProviderTest {
    private val xmlMapper = XmlMapper()

    private val mockEngine = MockEngine { request ->
        val mockResponse = javaClass.getResource("/MockPom.txt")?.toURI()?.let { File(it) }
        assertEquals("test.com", request.url.host)
        val pathSegments = request.url.encodedPath.removePrefix("/").split("/")
        assertEquals("com", pathSegments[0])
        assertEquals("codrut", pathSegments[1])
        assertEquals("test", pathSegments[2])
        assertEquals("test", pathSegments[3])
        assertEquals("1.0.0", pathSegments[4])

        respond(mockResponse?.readText() ?: "")
    }

    @BeforeEach
    fun setUp() {
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    @Test
    fun `should retrieve maven pom from the repository`() = testWithEngine(mockEngine) {
        config {
            install(ContentNegotiation) {
                xml()
            }
        }
        test { client: HttpClient ->
            val dependencyInfoProvider = MavenDependencyInfoProvider(client, xmlMapper, "https://test.com")

            val dependency = dependencyInfoProvider.getDependencyInfo("com.codrut.test:test:1.0.0")

            assertEquals("test", dependency.name)
            assertEquals("com.codrut.test:test:1.0.0", dependency.path)
            assertEquals(3, dependency.transientDependenciesPaths.size)
            assertEquals("com.codrut.test:mock-dependency1:1.0.0", dependency.transientDependenciesPaths[0])
            assertEquals("com.codrut.test:mock-dependency2:1.0.0", dependency.transientDependenciesPaths[1])
            assertEquals("com.codrut.test:mock-dependency3:1.0.0", dependency.transientDependenciesPaths[2])
        }
    }

}