package com.codrut.bob.the.builder.properties

import java.util.Properties

object MavenConfiguration {
    private var _properties = Properties()
    val url: String
        get() {
            if (_properties.isEmpty)
                _properties.load(javaClass.getResourceAsStream("application.properties"))
            return _properties.getProperty("mavenUrl")
        }

}