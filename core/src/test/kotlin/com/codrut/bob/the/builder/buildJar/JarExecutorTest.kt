package com.codrut.bob.the.builder.buildJar

import java.io.File
import java.util.jar.JarFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class JarExecutorTest {
    @Test
    internal fun executeSimpleJar() {
        val file = JarFile(javaClass.getResource("/SimpleJar.jar")?.toURI()?.let { File(it) })
        val executor = JarExecutor()

        val process = executor.execute(file)

        assertTrue(process.isAlive)
        assertEquals("/usr/bin/java", process.info().command().get())
        assertEquals("Hello World!", process.inputReader().readLine())
    }
}