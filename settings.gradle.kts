plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "BobTheBuilder"
include("core")
include("dependency-server")
include("local-dependency-manager")
include("maven-dependency-manager")
