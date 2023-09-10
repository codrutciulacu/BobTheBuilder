package com.codrut.bob.the.builder.graph

import java.util.UUID


data class Node(
    val id: UUID,
    val name: String,
    val path: String,
    val neighbours: List<Node>
)