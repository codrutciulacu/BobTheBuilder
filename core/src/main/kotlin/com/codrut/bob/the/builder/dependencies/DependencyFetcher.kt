package com.codrut.bob.the.builder.dependencies

import java.io.File

interface DependencyFetcher {
    suspend fun fetchDependencyBinary(dependency: Dependency): File
}