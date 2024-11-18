package topic

import CMSystemFiles.getTopicsMDNodes
import PATH_TO_SYSTEM_MD
import ParseUtil
import com.fasterxml.jackson.module.kotlin.readValue
import com.vladsch.flexmark.util.ast.Document
import common.TestUtil
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
    fun checkTopicStructureTest() {//TODO пофиксить этот тест
        assertTrue(
            tryValidateBySchema(
                PATH_TO_SYSTEM_MD + "/Схема по шаблону тематического узла.json",
                PATH_TO_SYSTEM_MD + "/Шаблон тематического узла.md"
            ).isSuccess
        )
    }

    public fun tryValidateBySchema(pathToSchema: String, pathToMD: String): Result<Unit> {

        val template: MDTDocumentNode =
            ParseUtil.jsonMapper.readValue<MDTDocumentNode>(Files.readString(Path.of(pathToSchema)))

        val docMd: Document = ParseUtil.mdParser.parse(Files.readString(Path.of(pathToMD)))

        return runCatching { docMd.validate(template) }
            .onFailure { println(it) }
    }
}