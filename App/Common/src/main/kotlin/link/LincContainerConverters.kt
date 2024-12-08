package link

import PATH_TO_DOCS_ROOT
import com.vladsch.flexmark.ast.Link
import com.vladsch.flexmark.ext.wikilink.WikiLink
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.util.function.Predicate.not
import java.util.regex.Pattern
import java.util.stream.Collectors
import kotlin.io.path.extension
import kotlin.io.path.name
import kotlin.io.path.nameWithoutExtension

val EXTENSIONS = Files.walk(Path.of(PATH_TO_DOCS_ROOT))
    .filter(Files::isRegularFile)
    .filter(Files::isReadable)
    .filter(not(Files::isDirectory))
    .filter { !it.extension.isNullOrBlank() }
    .map(Path::extension)
    .collect(Collectors.toSet())

fun WikiLink.toRelLinkContainer(pathToFile: Path): RelatedLinkContainer<WikiLink> {
    var rawPathStr = this.chars.substring(this.openingMarker.length, this.chars.indexOf(this.closingMarker))

    if (this.textSeparatorMarker.isNotBlank)
        rawPathStr = rawPathStr.substringBefore(this.textSeparatorMarker.toString())

    val path: Path = try {

        var fileName = rawPathStr.substringBefore("#")

        if (fileName.isBlank()) {
            pathToFile
        } else {
            if (fileName.filter { it in "#^[]|" }.isNotEmpty())
                throw RuntimeException("Wikilink[$fileName] не может содержать любой из символов #^[]|")

            findPathByFileNameOrThrow(pathToFile, fileName)
        }
    } catch (ex: Exception) {
        throw RuntimeException("Не удалось распарсить ссылку[${this.chars}] т.к. ${ex.message}")
    }

    return RelatedLinkContainer(
        this.chars.toString(),
        path.toUri(),
        true,
        path.toAbsolutePath().normalize(),
        rawPathStr.substringAfter("#", "")
            .split("#")
            .filter(String::isNotBlank)
            .map(Pattern::quote)
            .map { it.toRegex(RegexOption.IGNORE_CASE) }
            .toList(),
        this
    )
}

private fun findPathByFileNameOrThrow(pathToFile: Path, fileName: String): Path {

    val isWithExtension = EXTENSIONS.any { fileName.endsWith(it) }
    val mayBePath = if (isWithExtension)
        EXTENSIONS.map { pathToFile.relativeByCurrent(Path.of(fileName)) }.toMutableSet()
    else EXTENSIONS.map { pathToFile.relativeByCurrent(Path.of("$fileName.$it")) }.toMutableSet()

    mayBePath.add(pathToFile.relativeByCurrent(Path.of(fileName).toAbsolutePath().normalize()))

    return mayBePath.stream()
        .filter(Files::isRegularFile)
        .findAny()
        .orElseGet({
            Files.walk(Path.of(PATH_TO_DOCS_ROOT))
                .filter(Files::isRegularFile)
                .filter(Files::isReadable)
                .filter(not(Files::isDirectory))
                .filter { if (isWithExtension) it.name == fileName else it.nameWithoutExtension == fileName }
                .findAny()
                .orElseThrow({
                    RuntimeException("AnyLink[$fileName] такого файла не существует!")
                })
        })
}

//fun LinkRef.toRelLinkContainer(): RelatedLinkContainer<LinkRef> { TODO Пока никто не поддерживает формат
//    return RelatedLinkContainer("", Path.of(""), Path.of(""), "", this)
//}
private fun withoutExtension(fileName: String): String {
    return fileName.split(".").first()
}

/**
 *  @see [Примеры ссылок](../docs/system/Примеры%20ссылок#header-2.md)
 */
fun Link.toRelLinkContainer(pathToFile: Path): RelatedLinkContainer<Link> {

    val uri: URI = try {
        URI(this.url.toString())
    } catch (ex: Exception) {
        throw RuntimeException("Не удалось распарсить ссылку[${this.chars}] т.к. ${ex.message}")
    }

    if (!uri.scheme.isNullOrBlank())
        return RelatedLinkContainer(
            this.text.toString(),
            uri,
            false,
            pathToFile.relativeByCurrent(Path.of(uri.path)).toAbsolutePath().normalize(),
            emptyList(),
            this
        )

    if (uri.fragment.isNullOrBlank())
        return RelatedLinkContainer(
            this.text.toString(),
            uri,
            true,
            pathToFile.relativeByCurrent(findPathByFileNameOrThrow(pathToFile, uri.path)).toAbsolutePath().normalize(),
            emptyList(),
            this
        )

    val isEndedByDigitWithTire = "-\\d+$".toRegex().find(uri.fragment) != null

    val fragmentPattern =
        if (isEndedByDigitWithTire) uri.fragment.replace("-", "[-\\s]")
            .replace(
                "-\\d+$".toRegex(),
                { matchR -> "(-|\\s|\$)(${matchR.value.substring(1)})?" })
        else uri.fragment.replace("-", "[-\\s]")


    return RelatedLinkContainer(
        this.text.toString(),
        uri,
        true,
        pathToFile.relativeByCurrent(findPathByFileNameOrThrow(pathToFile, uri.path)).toAbsolutePath().normalize(),
        listOf(Pattern.quote(fragmentPattern).toRegex(RegexOption.IGNORE_CASE)),
        this
    )
}

/**
 * MD файлы указываются в тестах относительно модуля в котором запускается тест.
 * В MD документах ссылки указывают на другие MD файлы относительно их самих.
 * Данный метод позволяет получить путь по ссылкам относительно тестов.
 */
fun Path.relativeByCurrent(pathFromLink: Path): Path {
    val linkPathStr = pathFromLink.toString()
    val backStep = linkPathStr
        .split("/")
        .count { it == ".." }

    var cutPath = this.subpath(0, this.count() - backStep).toString()

    cutPath = cutPath.substringBeforeLast('/', "")

    if (!cutPath.startsWith("/") && !cutPath.startsWith("."))
        cutPath = "/" + cutPath

    return Path.of(cutPath + "/" + linkPathStr.substringAfterLast("../"))

}
