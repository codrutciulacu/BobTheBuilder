plugins {
    id("java")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}

tasks.test {
    useJUnitPlatform()
}


kotlin {
    jvmToolchain(17)
}


