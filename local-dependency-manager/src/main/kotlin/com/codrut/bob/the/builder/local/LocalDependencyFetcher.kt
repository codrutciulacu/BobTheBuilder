package com.codrut.bob.the.builder.dependencies.local

import com.codrut.bob.the.builder.dependencies.Dependency
import com.codrut.bob.the.builder.dependencies.DependencyFetcher
import java.io.File

class LocalDependencyFetcher : DependencyFetcher {
    override suspend fun fetchDependencyBinary(dependency: Dependency): File {
        TODO("Not yet implemented")
    }
}