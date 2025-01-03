package topic

import CMSystemFiles.getTopicsMDNodes
import PATH_TO_SYSTEM_MD
import ParseUtil
import ParseUtil.mdParser
import com.fasterxml.jackson.module.kotlin.readValue
import com.vladsch.flexmark.ast.Heading
import com.vladsch.flexmark.ast.Link
import com.vladsch.flexmark.ext.wikilink.WikiLink
import com.vladsch.flexmark.util.ast.Document
import com.vladsch.flexmark.util.ast.Node
import common.TestUtil
import getChildrenOfType
import link.RelatedLinkContainer
import link.toRelLinkContainer
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import tags.StructRoleNodeTag
import template.MDTDocumentNode
import template.validate
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.io.path.name
import kotlin.test.assertTrue

@DisplayName("Проверки для MD файлов в папке topic")
class CheckTopicTagsMandatoryTest {

    @Test
    @DisplayName("Все md узлы в папке topic, должны начинаться с Yaml Front Matter и обязан содержать tags #STRUCT/TOPIC")
    fun checkTopicRuleWhatYamlFrontMatterExistsTest() {
        TestUtil.checkOnlyOneTagYamlFrontMatterContainsTest(
            getTopicsMDNodes(),
            StructRoleNodeTag.STRUCT_TOPIC,
            false,
            false
        )
    }

    @Test
    @DisplayName("Шаблон тематического узла тоже должен соответствовать схеме составленной по нему")
    fun checkTopicStructureTest() {
        assertTrue(
            tryValidateBySchema(
                PATH_TO_SYSTEM_MD + "/Схема по шаблону тематического узла.json",
                Path.of(PATH_TO_SYSTEM_MD + "/Шаблон тематического узла.md")
            ).isSuccess
        )
        assertTrue(
            tryValidateBySchema(
                PATH_TO_SYSTEM_MD + "/Схема по шаблону тематического узла.json",
                Path.of(PATH_TO_SYSTEM_MD + "/Шаблон тематического узла v2.md")
            ).onFailure { println(it.message) }
                .isSuccess
        )
        assertTrue(
            tryValidateBySchema(
                PATH_TO_SYSTEM_MD + "/Схема по шаблону тематического узла.json",
                Path.of("src/test/kotlin/topic/Шаблон тематического узла WRONG v1.md")
            ).onFailure { println(it.message) }
                .isFailure
        )
    }

    @Test
    @DisplayName("Раздел '# Подтемы*:' должен содержать только ссылки на тематические MD узлы!")
    fun checkAllSubTopicIsLinkToTopicsLoopingTest() {
        var errors = mutableListOf<String>()

        getTopicsMDNodes().forEach { topicMDdoc ->

            val subTopicsStream = getPathsFromHeader(topicMDdoc, "Подтемы*:")

            if (subTopicsStream != null) {
                try {
                    TestUtil.checkOnlyOneTagYamlFrontMatterContainsTest(
                        subTopicsStream,
                        StructRoleNodeTag.STRUCT_TOPIC,
                        false,
                        false
                    )
                } catch (e: Throwable) {
                    errors.add("Документ $topicMDdoc содержит ссылку/и на подтемы, которые не являются тематическими узлами:\n${e.message}")
                }
            }
        }

        assertTrue(errors.isEmpty(), errors.joinToString("\n"))
    }

    @Test
    @DisplayName("Раздел '# Оценивающие узлы:' должен содержать только ссылки на оценивающие MD узлы!")
    fun checkEstimatingNodesHeaderContainsLinkToEstimatingNodesTest() {
        var errors = mutableListOf<String>()

        getTopicsMDNodes().forEach { topicMDdoc ->

            val subTopicsStream = getPathsFromHeader(topicMDdoc, "# Оценивающие узлы:")

            if (subTopicsStream != null) {
                try {
                    TestUtil.checkOnlyOneTagYamlFrontMatterContainsTest(
                        subTopicsStream,
                        StructRoleNodeTag.STRUCT_ESTIMATING_,
                        false,
                        true,
                        { StructRoleNodeTag.isEstimating(it!!.tag) }
                    )
                } catch (e: Throwable) {
                    errors.add("Документ $topicMDdoc содержит ссылку/и на узлы, которые не являются оценивающими:\n${e.message}")
                }
            }
        }

        assertTrue(errors.isEmpty(), errors.joinToString("\n"))
    }

    @Test
    @DisplayName("Раздел '# Информационные узлы:' должен содержать только ссылки на оценивающие MD узлы!")
    fun checkLearningNodesHeaderContainsLinkToLearningNodesTest() {
        var errors = mutableListOf<String>()

        getTopicsMDNodes().forEach { topicMDdoc ->

            val subTopicsStream = getPathsFromHeader(topicMDdoc, "# Информационные узлы:")

            if (subTopicsStream != null) {
                try {
                    TestUtil.checkOnlyOneTagYamlFrontMatterContainsTest(
                        subTopicsStream,
                        StructRoleNodeTag.STRUCT_LEARNING,
                        false,
                        true
                    )
                } catch (e: Throwable) {
                    errors.add("Документ $topicMDdoc содержит ссылку/и на узлы, которые не являются информационными:\n${e.message}")
                }
            }
        }

        assertTrue(errors.isEmpty(), errors.joinToString("\n"))
    }

    @Test
    @DisplayName("Цепочка тематических узлов в разделе '# Подтемы*:' не должна выстраиваться в петли!")
    fun checkAllSubTopicLoopingTest() {
        var errors = mutableListOf<String>()

        getTopicsMDNodes().forEach { rootTopicPath ->
            try {
                val stack = Stack<Path>()
                stack.push(rootTopicPath)
                iterateBySubTopicAndCheckLoop(stack, rootTopicPath)
            } catch (e: Exception) {
                errors.add(e.message ?: e.toString())
            }
        }

        assertTrue(errors.isEmpty(), errors.joinToString("\n"))
    }

    private fun iterateBySubTopicAndCheckLoop(stack: Stack<Path>, topic: Path) {
        getPathsFromHeader(topic, "Подтемы*:")?.forEach {
            if (stack.contains(it)) {
                stack.push(it)
                throw RuntimeException(
                    "Нельзя указывать на топик, который является предком во избежание петель!" +
                            "Последовательность с петлёй:[${stack.joinToString { it.name }}]"
                )
            }
            stack.push(it)
            iterateBySubTopicAndCheckLoop(stack, it)
            stack.pop()
        }

    }

    private fun getPathsFromHeader(topicMDdoc: Path, headerLabel: String) = mdParser.parse(Files.readString(topicMDdoc))
        .getChildrenOfType(Heading::class, { header -> header.chars.contains(headerLabel, true) })
        .firstOrNull()
        ?.getChildrenOfType<Node>(listOf(WikiLink::class, Link::class))
        ?.stream()
        ?.map {
            when (it) {
                is WikiLink -> it.toRelLinkContainer(topicMDdoc)
                is Link -> it.toRelLinkContainer(topicMDdoc)
                else -> throw RuntimeException("oops")
            }
        }
        ?.filter(RelatedLinkContainer<*>::isFile)
        ?.map(RelatedLinkContainer<*>::pathToFile)

    @Test
    @DisplayName("Все тематические узлы должны соответствовать схеме md документа'Схема по шаблону тематического узла.json'")
    fun checkAllTopicStructureTest() {
        val errors = mutableListOf<String>()
        getTopicsMDNodes().forEach { topicMDdoc ->
            tryValidateBySchema(
                PATH_TO_SYSTEM_MD + "/Схема по шаблону тематического узла.json",
                topicMDdoc
            ).onFailure { errors.add("Ошибка в ${topicMDdoc.fileName}\n" + it.message) }
                .isSuccess
        }
        assertTrue(errors.isEmpty(), errors.joinToString("\n"))
    }

    //TODO использовать эту функцию для проверки всех схем
    public fun tryValidateBySchema(pathToSchema: String, pathToMD: Path): Result<Unit> {

        val template: MDTDocumentNode =
            ParseUtil.jsonMapper.readValue<MDTDocumentNode>(Files.readString(Path.of(pathToSchema)))

        val docMd: Document = ParseUtil.mdParser.parse(Files.readString(pathToMD))

        return runCatching { docMd.validate(template) }
    }
}