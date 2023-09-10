package com.codrut.bob.the.builder.util

import com.codrut.bob.the.builder.dependencies.maven.POMEntity

fun POMEntity.buildAsDependencyPath() =
    "${groupId}:${artifactId}:${version}"

fun String.buildAsUrlPath() =
    this.split(".").joinToString("/")
