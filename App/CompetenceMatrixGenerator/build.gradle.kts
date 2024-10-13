plugins {
    application
    kotlin("jvm")
}

group = "ru.mc.generator"
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
    implementation(project(":Common"))
}

tasks.test {
    useJUnitPlatform()
}

//tasks.jar{
//    manifest{
//        attributes(mapOf("Main-Class" to "ru.mc.generator.MainKt"))
//    }
//}

kotlin {
    jvmToolchain(18)
}