package com.codrut.bob.the.builder.dependencies.local

import com.codrut.bob.the.builder.dependencies.Dependency
import com.codrut.bob.the.builder.dependencies.DependencyInfoProvider

class LocalDependencyInfoProvider : DependencyInfoProvider {
    override suspend fun getDependencyInfo(dependencyName: String): Dependency {
        TODO("Not yet implemented")
    }

}