package com.codrut.bob.the.builder.utils

import com.codrut.bob.the.builder.dependencies.Dependency
import com.codrut.bob.the.builder.graph.Node
import java.util.UUID

object Commons {
    val depA = Dependency("depA", "com.test:depA", emptyList())
    val depB =  Dependency("depB", "com.test:depB", listOf("depC"))
    val depC = Dependency("depC", "com.test:depC", listOf("depD", "depE"))
    val depD = Dependency("depD", "com.test:depD", emptyList())
    val depE = Dependency("depE", "com.test:depD", emptyList())
    val simpleGraph = Node(UUID.randomUUID(), depA.name, depA.path, emptyList())
    val complexGraph = Node(UUID.randomUUID(), depB.name, depB.path, listOf(
        Node(UUID.randomUUID(), depC.name, depC.path, listOf(
            Node(UUID.randomUUID(), depD.name, depD.path, emptyList()),
            Node(UUID.randomUUID(), depE.name, depE.path, emptyList())
        ))
    ))
}

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