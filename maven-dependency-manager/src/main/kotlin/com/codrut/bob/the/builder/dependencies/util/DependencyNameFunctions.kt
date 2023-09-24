package com.codrut.bob.the.builder.dependencies.util

import com.codrut.bob.the.builder.dependencies.maven.POMEntity

fun POMEntity.buildAsDependencyPath() =
    "${groupId}:${artifactId}:${version}"

fun String.buildAsUrlPath() =
    this.split(".").joinToString("/")
