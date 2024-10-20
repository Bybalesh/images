package ci.topic

import CMSystemFiles.getAllMDNodes
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterBlock
import com.vladsch.flexmark.util.ast.Node
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.nio.file.Files
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

@DisplayName("Все необходимые теги для #STRUCT/TOPIC присутствуют")
class CheckTagsMandatoryTest {

    @Test
    @DisplayName("Все md узлы, должны начинаться с Yaml Front Matter и обязан содержать tags")
    fun checkCommonRuleWhatYamlFrontMatterExistsTest() {

//        var isMDNodesExists = false
//
//        getAllMDNodes()
//            .forEach {
//                isMDNodesExists = true
//                var mdNode: Node = ParseUtil.mdParser.parse(Files.readString(it))
//                assertIs<YamlFrontMatterBlock>(mdNode.firstChild, "$it должен содержать Yaml Front Matter в заголовке")
//                assertFalse(
//                    mdNode.getAllFrontMatterNodesFromFirstFMBlock("tags").isEmpty(),
//                    "$it должен содержать tags в Yaml Front Matter"
//                )
//            }
//
//        assertTrue(isMDNodesExists, "$PATH_TO_DOCS_ROOT должен содержать MD файлы")
    }

}