package com.codrut.bob.the.builder

import com.codrut.bob.the.builder.dependencies.DependencyGraphBuilder
import com.codrut.bob.the.builder.dependencies.maven.MavenDependencyInfoProvider
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import com.fasterxml.jackson.databind.DeserializationConfig
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

class MavenDependenciesGraphBuildingTest {
    private val xmlMapper = XmlMapper()

    private val mockEngine = MockEngine { request ->
        val mockResponse = javaClass.getResource("${request.url.encodedPath}/deps.txt")?.toURI()?.let { File(it) }

        respond(mockResponse?.readText() ?: "")
    }

    @BeforeEach
    fun setUp() {
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        xmlMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }

    @Test
    fun `should build a dependency graph with all dependencies`() = testWithEngine(mockEngine) {
        config {
            install(ContentNegotiation) {
                xml()
            }
        }
        test { client: HttpClient ->
            val dependencyInfoProvider = MavenDependencyInfoProvider(client, xmlMapper, "https://test.com")

            val dependencyGraphBuilder = DependencyGraphBuilder(dependencyInfoProvider)

            val node = dependencyGraphBuilder.build("com.dep.test:test:1.0.0")

            assertEquals("test", node.name)
            assertEquals("com.dep.test:test:1.0.0", node.path)
            assertEquals(2, node.neighbours.size)
            assertEquals(1, node.neighbours[0].neighbours.size)
            assertEquals(0, node.neighbours[1].neighbours.size)
        }
    }


}