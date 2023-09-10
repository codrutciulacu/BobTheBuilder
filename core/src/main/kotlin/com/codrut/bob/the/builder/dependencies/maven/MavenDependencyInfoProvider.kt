package com.codrut.bob.the.builder.dependencies.maven

import com.codrut.bob.the.builder.dependencies.Dependency
import com.codrut.bob.the.builder.dependencies.DependencyInfoProvider
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

data class POMEntity(
    @JsonProperty("groupId")
    val groupId: String?,
    @JsonProperty("artifactId")
    val artifactId: String?,
    @JsonProperty("version")
    val version: String?,
    @JsonProperty("dependencies")
    val dependencies: List<POMEntity>?
)

fun mapPomToGenericDependency(pomEntity: POMEntity): Dependency =
    Dependency(
        name = pomEntity.artifactId ?: "",
        path = buildDependencyPath(pomEntity),
        pomEntity.dependencies?.map { buildDependencyPath(it) } ?: emptyList()
    )

private fun buildDependencyPath(it: POMEntity) =
    "${it.groupId}:${it.artifactId}:${it.version}"

class MavenDependencyInfoProvider(
    private val client: HttpClient,
    private val xmlMapper: XmlMapper,
    private val mavenRepoUrl: String
) : DependencyInfoProvider {

    override suspend fun getDependency(dependencyName: String): Dependency {
        val dependency = coroutineScope {
            val splittedDependencyName = dependencyName.split(":")
            val response = async {
                client.get(
                    buildDependencyUrl(
                        mavenRepoUrl,
                        splittedDependencyName[0],
                        splittedDependencyName[1],
                        splittedDependencyName[2]
                    )
                )
            }

            xmlMapper.readValue(response.await().bodyAsText(), POMEntity::class.java)
        }

        return mapPomToGenericDependency(dependency)
    }

    private fun buildDependencyUrl(
        mavenRepoUrl: String,
        dependencyGroup: String,
        dependencyName: String,
        version: String
    ): String =
        "$mavenRepoUrl/${dependencyGroup.buildAsUrlPath()}/${dependencyName.buildAsUrlPath()}/$version"

    private fun String.buildAsUrlPath() =
        this.split(".").joinToString("/")

}