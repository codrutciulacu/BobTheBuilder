package com.codrut.bob.the.builder.dependencies.maven

import com.codrut.bob.the.builder.dependencies.Dependency
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.tests.utils.config
import io.ktor.client.tests.utils.test
import io.ktor.client.tests.utils.testWithEngine
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertEquals
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class MavenDependencyFetcherTest {

    private val mockEngine = MockEngine { request ->

        respond(
            content = ByteReadChannel("""heeeeeeeeeelloooooooooo"""),
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/octet-stream")
        )
    }

    @Test
    fun `should retrieve the dependency correctly`() = testWithEngine(mockEngine) {
        config {
        }

        test { client: HttpClient ->
            val mavenDependencyFetcher = MavenDependencyFetcher(client, Path.of("/tmp/"), "https://local.com")

            val fetchDependencyBinary = mavenDependencyFetcher.fetchDependencyBinary(
                Dependency(
                    name = "test",
                    path = "com.codrut.test:test:1.0.0",
                    transientDependenciesPaths = emptyList()
                )
            )

            assertEquals("/tmp/test.jar", fetchDependencyBinary.absolutePath)
            assertEquals(23, Files.size(fetchDependencyBinary.toPath()))
        }
    }

    @AfterEach
    fun tearDown() {
        Files.delete(Path.of("/tmp/test.jar"))
    }
}