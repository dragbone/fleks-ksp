plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp").version("2.1.0-1.0.29")
}

group = "com.dragbone"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.quillraven.fleks:Fleks:2.11")
    ksp(project(":ksp"))
}

kotlin {
    jvmToolchain(19)
}