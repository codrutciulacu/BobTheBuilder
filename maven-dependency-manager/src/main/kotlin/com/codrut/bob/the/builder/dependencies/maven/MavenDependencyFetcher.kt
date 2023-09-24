package com.codrut.bob.the.builder.dependencies.maven

import com.codrut.bob.the.builder.dependencies.Dependency
import com.codrut.bob.the.builder.dependencies.DependencyFetcher
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.writeBytes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MavenDependencyFetcher(
    private val client: HttpClient,
    private val cachePath: Path,
    private val mavenRepoUrl: String
) : DependencyFetcher {
    override suspend fun fetchDependencyBinary(dependency: Dependency): File {
        val bytes = client.get(mavenRepoUrl).bodyAsChannel()
        val tempFile = withContext(Dispatchers.IO) {
            Files.createFile(Path.of("$cachePath/${dependency.name}.jar"))
        }

        val byteBufferSize = 1024 * 100
        val byteBuffer = ByteArray(byteBufferSize)

        var read = 0

        do {
            val currentRead = bytes.readAvailable(byteBuffer, 0, byteBufferSize)
            if (currentRead > 0) {
                tempFile.writeBytes(if (currentRead < byteBufferSize) {
                    byteBuffer.sliceArray(0 until currentRead)
                } else {
                    byteBuffer
                })

                read += currentRead
            }
        } while(currentRead >= 0)

        return tempFile.toFile()
    }
}