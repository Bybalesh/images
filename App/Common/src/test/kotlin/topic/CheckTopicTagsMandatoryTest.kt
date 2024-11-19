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
import kotlin.test.assertTrue

@DisplayName("Проверки для MD файлов в папке topic")
class CheckTopicTagsMandatoryTest {

    @Test
    @DisplayName("Все md узлы в папке topic, должны начинаться с Yaml Front Matter и обязан содержать tags #STRUCT/TOPIC")
    fun checkTopicRuleWhatYamlFrontMatterExistsTest() {
        TestUtil.checkOnlyOneTagYamlFrontMatterContainsTest(
            getTopicsMDNodes(),
            StructRoleNodeTag.STRUCT_TOPIC,
            false
        )
    }

    @Test
    @DisplayName("Шаблон тематического узла тоже должен соответствовать схеме составленной по нему")
    fun checkTopicStructureTest() {
        assertTrue(
            tryValidateBySchema(
                PATH_TO_SYSTEM_MD + "/Схема по шаблону тематического узла.json",
                Path.of(PATH_TO_SYSTEM_MD + "/Шаблон тематического узла v1.md")
            ).isSuccess
        )
        assertTrue(
            tryValidateBySchema(
                PATH_TO_SYSTEM_MD + "/Схема по шаблону тематического узла.json",
                Path.of(PATH_TO_SYSTEM_MD + "/Шаблон тематического узла v2.md")
            ).isSuccess
        )
    }

    @Test
    @DisplayName("Цепочка тематических узлов в разделе '# Подтемы*:' не должна выстраиваться в петли!")
    fun checkAllSubTopicLoopingTest() {
        val errors = mutableListOf<String>()
        getTopicsMDNodes().forEach { topicMDdoc ->
            val aa =
                mdParser.parse(Files.readString(topicMDdoc))
                    .getChildrenOfType(Heading::class, { header -> header.chars.contains("Подтемы*:", true) })
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
                    ?.toList()
            println()//TODO обнаружить петлю

        }
        assertTrue(errors.isEmpty(), errors.joinToString("\n"))
    }

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