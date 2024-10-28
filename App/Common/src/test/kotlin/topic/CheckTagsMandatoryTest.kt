package topic

import CMSystemFiles.getAllMDNodes
import PATH_TO_DOCS_ROOT
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterBlock
import com.vladsch.flexmark.util.ast.Node
import getAllFrontMatterNodesFromFirstFMBlock
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.nio.file.Files
import kotlin.test.assertIs

@DisplayName("Все необходимые теги для #STRUCT/TOPIC присутствуют")
class CheckTagsMandatoryTest {

    @Test//TODO реализовать
    @DisplayName("Все md узлы в папке topic, должны начинаться с Yaml Front Matter и обязан содержать tags #STRUCT/TOPIC")
    fun checkCommonRuleWhatYamlFrontMatterExistsTest() {
        var isMDNodesExists = false
        getAllMDNodes()//TODO реализовать
            .forEach {
                isMDNodesExists = true
                var mdNode: Node = ParseUtil.mdParser.parse(Files.readString(it))
                assertIs<YamlFrontMatterBlock>(mdNode.firstChild, "$it должен содержать Yaml Front Matter в заголовке")
                assertFalse(
                    mdNode.getAllFrontMatterNodesFromFirstFMBlock("tags").isEmpty(),
                    "$it должен содержать tags в Yaml Front Matter"
                )
            }

        assertTrue(isMDNodesExists, "$PATH_TO_DOCS_ROOT должен содержать MD файлы")
    }

}