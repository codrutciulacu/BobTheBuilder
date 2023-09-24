package com.codrut.bob.the.builder.dependencies

interface DependencyInfoProvider {
    suspend fun getDependencyInfo(dependencyName: String): Dependency
}