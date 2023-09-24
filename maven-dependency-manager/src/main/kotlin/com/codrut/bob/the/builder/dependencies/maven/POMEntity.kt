package com.codrut.bob.the.builder.dependencies.maven

import com.codrut.bob.the.builder.dependencies.Dependency
import com.codrut.bob.the.builder.dependencies.util.buildAsDependencyPath
import com.fasterxml.jackson.annotation.JsonProperty

data class POMEntity(
    @JsonProperty("groupId")
    val groupId: String?,
    @JsonProperty("artifactId")
    val artifactId: String?,
    @JsonProperty("version")
    val version: String?,
    @JsonProperty("com/codrut/bob/the/builder/dependencies")
    val dependencies: List<POMEntity>?
) {
    fun mapPomToGenericDependency(): Dependency =
        Dependency(
            name = artifactId ?: "",
            path = buildAsDependencyPath(),
            dependencies?.map { it.buildAsDependencyPath() } ?: emptyList()
        )

}