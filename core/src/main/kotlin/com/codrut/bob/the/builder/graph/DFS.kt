package com.codrut.bob.the.builder.graph

import java.util.UUID

class DFS(
    val visited: MutableList<UUID>
) {
    fun visit(node: Node) {
        visited.add(node.id)

        for(neigh in node.neighbours) {
            if(neigh.id !in visited) {
                visit(node)
            }
        }
    }
}