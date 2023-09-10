package com.codrut.bob.the.builder.dependencies

interface DependencyInfoProvider {
    suspend fun getDependency(dependencyName: String): Dependency
}