group = "com.vladsch.flexmark"
version = "1.0-SNAPSHOT"

dependencies {
    // Markdown convertor (Flexmark)
    val versionLibraryFlexmark = "0.64.8"
// https://github.com/vsch/flexmark-java/releases
    api("com.vladsch.flexmark:flexmark:${versionLibraryFlexmark}")
    api("com.vladsch.flexmark:flexmark-util:${versionLibraryFlexmark}")
}