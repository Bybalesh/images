package topic

import CMSystemFiles.getSystemsMDNodes
import CMSystemFiles.getTopicsMDNodes
import ParseUtil
import com.vladsch.flexmark.util.ast.Document
import common.TestUtil
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import tags.StructRoleNodeTag
import template.MDTDocumentNode
import template.toTemplate
import java.nio.file.Files
import kotlin.io.path.nameWithoutExtension

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
    @DisplayName("Все md узлы в папке topic, должны начинаться с Yaml Front Matter и обязан содержать tags #STRUCT/TOPIC")
    fun checkTopicStructureTest() {

        val topicMdPath = getSystemsMDNodes()
            .filter { it.nameWithoutExtension == "Шаблон тематического узла" }
            .findAny()
            .get()


        val topicMdTemplate = ParseUtil.mdParser.parse(Files.readString(topicMdPath)).toTemplate()

        val json = """
            {
              "children": [
                {
                  "children": [],
                  "qualifiedName": "template.MDTListNode"
                }
              ],
              "qualifiedName": "template.MDTDocumentNode"
            }
        """

//        = ParseUtil.jsonMapper.writeValueAsString(topicMdTemplate)
//        println(ParseUtil.jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(topicMdTemplate))
        val obj = ParseUtil.jsonMapper.readValue(json, MDTDocumentNode::class.java)

        getTopicsMDNodes()


    }

}