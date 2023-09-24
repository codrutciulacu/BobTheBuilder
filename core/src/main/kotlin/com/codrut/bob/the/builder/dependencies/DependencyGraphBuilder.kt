package com.codrut.bob.the.builder.dependencies

import com.codrut.bob.the.builder.graph.Node
import java.util.UUID

class DependencyGraphBuilder(
    val dependencyInfoProvider: DependencyInfoProvider
) {
    suspend fun build(dependencyName: String): Node {
        val dependency = dependencyInfoProvider.getDependencyInfo(dependencyName)

        return Node(
            id = UUID.randomUUID(),
            name = dependency.name,
            path = dependency.path,
            neighbours = dependency.transientDependenciesPaths.map { build(it) }
        )
    }
}