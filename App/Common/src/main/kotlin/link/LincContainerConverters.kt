package link

import PATH_TO_DOCS_ROOT
import com.vladsch.flexmark.ast.Link
import com.vladsch.flexmark.ext.wikilink.WikiLink
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.util.function.Predicate.not
import java.util.stream.Collectors
import kotlin.io.path.extension
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

        var pathStr = rawPathStr.substringBefore("#")

        if (pathStr.filter { it in "#^[]|" }.isNotEmpty())
            throw RuntimeException("Wikilink[$pathStr] не может содержать любой из символов #^[]|")

        val mayBePath = EXTENSIONS.map { pathToFile.relativeByCurrent(Path.of("$pathStr.$it")) }.toMutableSet()
        mayBePath.add(pathToFile.relativeByCurrent(Path.of(pathStr)))

        mayBePath.stream()
            .filter(Files::isRegularFile)
            .findAny()
            .orElseGet({
                Files.walk(Path.of(PATH_TO_DOCS_ROOT))
                    .filter(Files::isRegularFile)
                    .filter(Files::isReadable)
                    .filter(not(Files::isDirectory))
                    .filter { it.nameWithoutExtension == pathStr }
                    .findAny()
                    .orElseThrow({ RuntimeException("Wikilink[$pathStr] такого файла не существует!") })//TODO здесь не учитываются дубли
            })

    } catch (ex: Exception) {
        throw RuntimeException("Не удалось распарсить ссылку[${this.chars}] т.к. ${ex.message}")
    }

    return RelatedLinkContainer(
        this.chars.toString(),
        path.toUri(),
        true,
        path,
        rawPathStr.substringAfter("#", "")
            .split("#")
            .filter(String::isNotBlank)
            .map { it.toRegex(RegexOption.IGNORE_CASE) }
            .toList(),
        this
    )
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
            Path.of(uri.path),
            null,
            this
        )

    if (uri.fragment.isNullOrBlank())
        return RelatedLinkContainer(
            this.text.toString(),
            uri,
            true,
            Path.of(uri.path),
            null,
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
        pathToFile.relativeByCurrent(Path.of(uri.path)),
        listOf(fragmentPattern.toRegex(RegexOption.IGNORE_CASE)),
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

    return Path.of(cutPath + "/" + linkPathStr.substringAfterLast("../"))

}
