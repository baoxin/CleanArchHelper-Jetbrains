plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij") version "1.17.3"
}

group = "com.baoxin"
version = "1.1.2"

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
dependencies {
    // Add Gson for JSON serialization
    implementation("com.google.code.gson:gson:2.10.1")
}

intellij {
    version.set("2024.2.5")
    type.set("IC") // IntelliJ IDEA Community Edition

    pluginName.set("Clean Architecture Helper")
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("242")
        untilBuild.set("252.*")

        changeNotes.set("""
            Updated version of Clean Architecture Helper plugin.

            Changes:
            - Updated to IntelliJ Platform 2024.2.5
            - Updated Kotlin to 1.9.25
            - Updated IntelliJ Gradle Plugin to 2.0.1
            - Expanded compatibility to newer IDE versions

            Features:
            - Create Clean Architecture base structure
            - Create Feature modules with standard three-layer architecture
            - Configurable directory structures
            - Support for Flutter, React, Node.js projects
        """.trimIndent())
    }
}
