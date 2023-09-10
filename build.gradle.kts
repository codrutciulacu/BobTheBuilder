plugins {
    id("java")
    kotlin("jvm") version "1.9.0"
}

group = "com.codrut"
version = "0.0.1"

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin="org.jetbrains.kotlin.jvm")
    apply(plugin="java")

    repositories {
        mavenCentral()
    }

    dependencies {
        val gsonVersion: String by project
        val jacksonVersion: String by project
        val junitVersion: String by project

        implementation("com.google.code.gson:gson:$gsonVersion")
        implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:$jacksonVersion")

        testImplementation(platform("org.junit:junit-bom:$junitVersion"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }
}