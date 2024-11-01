plugins {
    kotlin("jvm") version "2.0.20"
}

tasks.wrapper {
    gradleVersion = "8.8"
    distributionUrl =
        "https://services.gradle.org/distributions/gradle-8.8-bin.zip" //TODO потом применить внутри компании
}

group = "ru.mc"
version = "1.0-SNAPSHOT"

subprojects {
    apply {
        plugin("kotlin")
    }

    repositories {
        mavenCentral()
        maven {
            url = uri("https://www.jetbrains.com/intellij-repository/releases")
        }
        maven {
            url = uri("https://www.jetbrains.com/intellij-repository/snapshots")
        }
        maven {
            url = uri("https://cache-redirector.jetbrains.com/intellij-dependencies")
        }
    }
    kotlin {
        jvmToolchain(18)
    }
    dependencies {
        testImplementation(kotlin("test"))
    }
    tasks.test {
        testLogging {
            events("passed", "skipped", "failed", "standardOut", "standardError")
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
        useJUnitPlatform()
    }
}
