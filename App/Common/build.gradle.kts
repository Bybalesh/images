plugins {
    kotlin("jvm")
}

group = "ru.mc.common"
version = "1.0-SNAPSHOT"

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

dependencies {
    testImplementation(kotlin("test"))
    api("org.jetbrains:markdown:0.7.3")
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.module/jackson-module-kotlin
    api("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.0")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(18)
}