package com.codrut.bob.the.builder.dependencies.maven

import com.codrut.bob.the.builder.dependencies.Dependency
import com.codrut.bob.the.builder.dependencies.DependencyInfoProvider
import com.codrut.bob.the.builder.dependencies.util.buildAsUrlPath
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class MavenDependencyInfoProvider(
    private val client: HttpClient,
    private val xmlMapper: XmlMapper,
    private val mavenRepoUrl: String
) : DependencyInfoProvider {

    override suspend fun getDependencyInfo(dependencyName: String): Dependency {
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

        return dependency.mapPomToGenericDependency()
    }

    private fun buildDependencyUrl(
        mavenRepoUrl: String,
        dependencyGroup: String,
        dependencyName: String,
        version: String
    ): String =
        "$mavenRepoUrl/${dependencyGroup.buildAsUrlPath()}/${dependencyName.buildAsUrlPath()}/$version"

}