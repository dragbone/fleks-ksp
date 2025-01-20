plugins {
    kotlin("jvm")
}

group = "com.dragbone"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup:kotlinpoet:2.0.0")
    implementation("com.squareup:kotlinpoet-ksp:2.0.0")
    implementation("com.google.devtools.ksp:symbol-processing-api:2.1.0-1.0.29")
}

kotlin {
    jvmToolchain(19)
}