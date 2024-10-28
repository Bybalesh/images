plugins {
    kotlin("jvm")
}

group = "com.vladsch.flexmark"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    // Markdown convertor (Flexmark)
    val versionLibraryFlexmark = "0.64.8"
// https://github.com/vsch/flexmark-java/releases
    api("com.vladsch.flexmark:flexmark:${versionLibraryFlexmark}")
    api("com.vladsch.flexmark:flexmark-util:${versionLibraryFlexmark}")
//    flexmark-ext-typographic TODO возможно добавить
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(18)
}