group = "ru.mc.common"
version = "1.0-SNAPSHOT"

dependencies {
    api("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.1")
    // Markdown convertor (Flexmark)
    val versionLibraryFlexmark = "0.64.8"
// https://github.com/vsch/flexmark-java/releases
    api("com.vladsch.flexmark:flexmark:${versionLibraryFlexmark}")
    api("com.vladsch.flexmark:flexmark-util:${versionLibraryFlexmark}")
    api("com.vladsch.flexmark:flexmark-ext-tables:${versionLibraryFlexmark}")
    api("com.vladsch.flexmark:flexmark-ext-gfm-strikethrough:${versionLibraryFlexmark}")
    api("com.vladsch.flexmark:flexmark-ext-yaml-front-matter:${versionLibraryFlexmark}")
    api("com.vladsch.flexmark:flexmark-ext-wikilink:${versionLibraryFlexmark}")
    api("com.vladsch.flexmark:flexmark-ext-footnotes:${versionLibraryFlexmark}")
    api("com.vladsch.flexmark:flexmark-ext-gfm-tasklist:${versionLibraryFlexmark}")
    api(project(":flexmark-ext-tag"))
}