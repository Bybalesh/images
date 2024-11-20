package common

import CMSystemFiles.getAllMDNodes
import ParseUtil
import com.vladsch.flexmark.ast.Heading
import com.vladsch.flexmark.ast.Link
import com.vladsch.flexmark.ast.LinkRef
import com.vladsch.flexmark.ext.wikilink.WikiLink
import getChildrenOfType
import link.RelatedLinkContainer
import link.toRelLinkContainer
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.net.URI
import java.nio.file.Files
import kotlin.io.path.name
import kotlin.test.assertTrue

@DisplayName("Общие структурные правила касающиеся ссылок для всех узлов")
class CheckCommonNodesForLinksTest {

    @Test
    @DisplayName("Ссылки формата [label](link) WikiLink::class должны указывать на существующий файл и заголовок")
    fun checkCommonFileAndHeaderByWikiLinkExistsTest() {
        val errMsg = mutableListOf<String>()

        getAllMDNodes()
            .filter{!it.name.contains("Примеры ссылок")}
            .forEach path@{ path ->
                try {
                    ParseUtil.mdParser.parse(Files.readString(path))
                        .getChildrenOfType(WikiLink::class)
                        .stream()
                        .map { it.toRelLinkContainer(path) }
                        .filter(RelatedLinkContainer<WikiLink>::isFile)
                        .forEach rlc@{ rlc ->
                            var someHeader: Heading? = null
                            rlc.mDElementRegex?.forEach { regex ->
                                if (someHeader != null) {
                                    someHeader = someHeader!!.getChildrenOfType(Heading::class)
                                        .find { regex.matches(it.text) }
                                } else {
                                    someHeader = ParseUtil.mdParser.parse(Files.readString(rlc.pathToFile))
                                        .getChildrenOfType(Heading::class)
                                        .find { regex.matches(it.text) }
                                }
                            }
                            if (rlc.mDElementRegex!!.isNotEmpty())
                                if (someHeader == null || !rlc.mDElementRegex!!.last().matches(someHeader!!.text))
                                    errMsg.add(
                                        "В документе:[$path] на строке:[${rlc.targetLink.lineNumber}] есть ссылка на заголовок, которого нет:[$rlc]."
                                    )
                        }
                } catch (ex: Exception) {
                    errMsg.add("В документе:[$path] не удалось проверить ссылку, потому что :[${ex}].")
                }
            }

        assertTrue(errMsg.isEmpty(), errMsg.joinToString("\n"))
    }

    @Test
    @DisplayName("Ссылки формата [label](link) LinkRef::class должны содержать валидные символы в link")
    fun checkCommonBrokenLinksTest() {
        val errMsg = mutableListOf<String>()

        getAllMDNodes()
            .filter { !it.fileName.toString().contains("Шпаргалка по синтаксису разметки Markdown") }
            .filter { !it.fileName.toString().contains("Примеры ссылок") }
            .forEach { path ->
                try {
                    ParseUtil.mdParser.parse(Files.readString(path))
                        .getChildrenOfType(LinkRef::class)
                        .filter { it.chars.startsWith("[") && it.chars.endsWith("]") }
                        .filter { it.next != null }
                        .filter { it.next!!.chars.startsWith("(") && it.next!!.chars.contains(")") }
                        .filter {
                            runCatching {
                                URI(
                                    it.next!!.chars.substring(
                                        1,
                                        it.next!!.chars.indexOf(')')
                                    )
                                )
                            }.isFailure
                        }
                        .forEach {
                            val reason = runCatching { URI(it.next!!.chars.substring(1, it.next!!.chars.indexOf(')'))) }
                                .getOrElse { exception -> exception.localizedMessage }
                            val linkText = it.chars.toString() + it.next!!.chars.toString()
                            errMsg.add(
                                """
                            |На строке:[${it.lineNumber}] есть сломанная ссылка:[$linkText] в файле:[$path].
                            |Причина: [$reason]
                        """
                            )
                        }
                } catch (ex: Exception) {
                    errMsg.add("В документе:[$path] удалось проверить ссылку, потому что :[${ex}].")
                }
            }

        assertTrue(errMsg.isEmpty(), errMsg.joinToString("\n"))
    }

    @Test
    @DisplayName("Ссылки формата [label](link) Link::class должны указывать на существующий файл и заголовок")
    fun checkCommonFileAndHeaderByLinkExistsTest() {
        val errMsg = mutableListOf<String>()

        getAllMDNodes()
            .filter { !it.fileName.toString().contains("Шпаргалка по синтаксису разметки Markdown") }
            .filter { !it.fileName.toString().contains("Примеры ссылок") }
            .forEach path@{ path ->
                try {
                    ParseUtil.mdParser.parse(Files.readString(path))
                        .getChildrenOfType(Link::class)
                        .stream()
                        .map { it.toRelLinkContainer(path) }
                        .filter(RelatedLinkContainer<Link>::isFile)
                        .forEach rlc@{ rlc ->
                            if (!Files.exists(rlc.pathToFile)) {
                                errMsg.add(
                                    "В документе:[$path] на строке:[${rlc.targetLink.lineNumber}] есть ссылка на несуществующий файл:[$rlc]."
                                )
                                return@rlc
                            }
                            if (rlc.mDElementRegex!!.isNotEmpty())
                                if (
                                    !ParseUtil.mdParser.parse(Files.readString(rlc.pathToFile))
                                        .getChildrenOfType(Heading::class)
                                        .any { rlc.mDElementRegex!!.first().matches(it.text) }
                                )
                                    errMsg.add(
                                        "В документе:[$path] на строке:[${rlc.targetLink.lineNumber}] есть ссылка на заголовок, которого нет:[$rlc]."
                                    )
                        }
                } catch (ex: Exception) {
                    errMsg.add("В документе:[$path] удалось проверить ссылку, потому что :[${ex}].")
                }
            }

        assertTrue(errMsg.isEmpty(), errMsg.joinToString("\n"))
    }
}

