plugins {
    id("java")
}

configurations {
    create("intTestsRuntimeOnly") {
        extendsFrom(configurations.testRuntimeOnly.get())
    }

    create("intTestsImplementation") {
        isCanBeResolved = true
    }
}

val intTestsImplementation by configurations.getting {
    extendsFrom(configurations.testImplementation.get())
}

val intTestsRuntimeOnly by configurations.getting {
}

sourceSets {
    create("intTests") {
        kotlin.srcDir("$projectDir/src/intTests/kotlin")
        resources.srcDir("$projectDir/src/intTests/resources")
        compileClasspath += main.get().output + test.get().output
        runtimeClasspath += main.get().output + test.get().output
    }
}

dependencies {
    val ktorVersion: String by project
    val junitVersion: String by project

    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-xml:$ktorVersion")

    testImplementation("io.ktor:ktor-client-tests:$ktorVersion")

    intTestsImplementation("io.ktor:ktor-client-tests:$ktorVersion")
    intTestsImplementation(platform("org.junit:junit-bom:$junitVersion"))
    intTestsImplementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    intTestsImplementation("org.junit.jupiter:junit-jupiter")
    intTestsImplementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    intTestsImplementation("io.ktor:ktor-serialization-kotlinx-xml:$ktorVersion")
}

tasks.test {
    useJUnitPlatform()
}

val intTests = task<Test>("intTests") {
    description = "Runs the integration tests"
    group = "verification"

    testClassesDirs = sourceSets["intTests"].output.classesDirs
    classpath = sourceSets["intTests"].runtimeClasspath
    mustRunAfter(tasks["test"])

    useJUnitPlatform()
}

val compileIntTestsKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileIntTestsKotlin.kotlinOptions {
    jvmTarget = "17"
    freeCompilerArgs = listOf("-Xemit-jvm-type-annotations")
}

tasks.withType<ProcessResources> {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}


tasks.check { dependsOn(intTests) }

kotlin {
    jvmToolchain(17)
}


