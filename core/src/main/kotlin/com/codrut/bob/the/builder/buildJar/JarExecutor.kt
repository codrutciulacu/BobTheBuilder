package com.codrut.bob.the.builder.buildJar

import java.util.jar.JarFile

class JarExecutor {
    fun execute(jarFile: JarFile): Process {
        return Runtime.getRuntime().exec("java -jar ${jarFile.name}")
    }
}