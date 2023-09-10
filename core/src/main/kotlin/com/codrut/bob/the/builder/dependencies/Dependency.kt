package com.codrut.bob.the.builder.dependencies

data class Dependency(
    val name: String,
    val path: String,
    val transientDependenciesPaths: List<String>
)