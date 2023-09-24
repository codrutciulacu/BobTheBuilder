plugins {
    id("java")
}

dependencies {
    val ktorVersion: String by project
    val junitVersion: String by project

    implementation(project(":core"))
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-xml:$ktorVersion")

    testImplementation("io.ktor:ktor-client-tests:$ktorVersion")
}

tasks.test {
    useJUnitPlatform()
}