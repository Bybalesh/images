plugins {
    application
}

group = "ru.mc.generator"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":Common"))
}

//tasks.jar{
//    manifest{
//        attributes(mapOf("Main-Class" to "ru.mc.generator.MainKt"))
//    }
//}

