plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "App"
include("Common")
include("CompetenceMatrixGenerator")
include("flexmark-ext-tag")
