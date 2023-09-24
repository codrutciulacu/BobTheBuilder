package com.codrut.bob.the.builder.dependencies

import com.codrut.bob.the.builder.graph.Node
import com.codrut.bob.the.builder.utils.Commons.complexGraph
import com.codrut.bob.the.builder.utils.Commons.depA
import com.codrut.bob.the.builder.utils.Commons.depB
import com.codrut.bob.the.builder.utils.Commons.depC
import com.codrut.bob.the.builder.utils.Commons.depD
import com.codrut.bob.the.builder.utils.Commons.depE
import com.codrut.bob.the.builder.utils.Commons.simpleGraph
import java.util.UUID
import javax.naming.directory.InvalidAttributesException
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FakeDependencyInfoProvider : DependencyInfoProvider {
    override suspend fun getDependencyInfo(dependencyName: String): Dependency = when (dependencyName) {
        "depA" -> depA
        "depB" -> depB
        "depC" -> depC
        "depD" -> depD
        "depE" -> depE
        else -> throw InvalidAttributesException("Cannot find the needed dependency")
    }
}

class DependencyGraphBuilderTest {
    @Test
    fun buildSimpleDepsGraph() = runBlocking {
        val dependencyGraphBuilder = DependencyGraphBuilder(FakeDependencyInfoProvider())

        assertNode(simpleGraph, dependencyGraphBuilder.build("depA"))
    }

    @Test
    fun buildComplexDepsGraph() = runBlocking{
        val dependencyGraphBuilder = DependencyGraphBuilder(FakeDependencyInfoProvider())

        val expected = complexGraph
        val actual = dependencyGraphBuilder.build("depB")

        val expectedTopologicalSort = mutableListOf<String>()
        buildTopologicalSort(expected, mutableListOf(), expectedTopologicalSort)

        val actualTopologicalSort = mutableListOf<String>()
        buildTopologicalSort(actual, mutableListOf(), actualTopologicalSort)

        assertEquals(expectedTopologicalSort.reversed(), actualTopologicalSort.reversed())
        assertEquals(4, actualTopologicalSort.size)
    }

    private fun buildTopologicalSort(node: Node, visited: MutableList<UUID>, topologicalSort: MutableList<String>) {
        visited.add(node.id)

        for(neigh in node.neighbours) {
            if(neigh.id !in visited) {
                buildTopologicalSort(neigh, visited, topologicalSort)
            }
        }

        topologicalSort.add(node.name)
    }

    companion object {
        fun assertNode(expected: Node, actual: Node) {
            assertEquals(expected.name, actual.name)
            assertEquals(expected.path, actual.path)
            val neigh = expected.neighbours.filter { it.name !in actual.neighbours.map { it.name } }
            assertTrue(neigh.isEmpty())
        }

    }
}