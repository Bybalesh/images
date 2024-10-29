package common

import CMSystemFiles.getAllMDNodes
import PATH_TO_DOCS_ROOT
import PATH_TO_TAGS_DESCRIPTION_MD
import ParseUtil
import TagUtil.getTagsEnums
import com.vladsch.flexmark.ext.tag.Tag
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterBlock
import com.vladsch.flexmark.util.ast.Node
import getAllFrontMatterNodesFromFirstFMBlock
import getChildrenOfType
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import tags.LanguageTag
import tags.StructRoleNodeTag
import java.nio.file.Files
import kotlin.io.path.name
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

@DisplayName("Общие структурные правила касающиеся тегов для всех узлов")
class CheckCommonNodesStructureRulesTest {

    @Test
    @DisplayName("Все md узлы, должны начинаться с Yaml Front Matter и обязан содержать tags")
    fun checkCommonRuleWhatYamlFrontMatterExistsTest() {

        var isMDNodesExists = false

        getAllMDNodes()
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

    @Test
    @DisplayName("Все md узлы, кроме шаблонов, должны содержать в Yaml Front Matter тег с одной из структурных ролей узла")
    fun checkCommonRuleWhatYamlFrontMatterContainsStructureTagTest() {
        getAllMDNodes()
            .filter { !it.name.startsWith("Шаблон") }
            .forEach { path ->
                val tagsCnt = ParseUtil.mdParser.parse(Files.readString(path))
                    .getAllFrontMatterNodesFromFirstFMBlock("tags")
                    .map("#"::plus)
                    .filter { StructRoleNodeTag.tagOf(it) != null }

                assertEquals(
                    1,
                    tagsCnt.size,
                    "$path содержит ${tagsCnt.size} шт StructRoleNodeTag:[$tagsCnt]. Должен 1! "
                )
            }
    }

    @Test
    @DisplayName("Все md узлы  должны содержать в Yaml Front Matter тег только те теги у которых есть описание в $PATH_TO_TAGS_DESCRIPTION_MD ")
    fun checkCommonRuleWhatAllTagsYamlFrontMatterHaveDescriptionTest() {
        val tagsEnums = getTagsEnums()
        getAllMDNodes()
            .forEach { path ->
                val tagsWithoutDescription = ParseUtil.mdParser.parse(Files.readString(path))
                    .getAllFrontMatterNodesFromFirstFMBlock("tags")
                    .map("#"::plus)
                    .filter { mayBeTag -> !tagsEnums.any { it.tagOf(mayBeTag) != null } }

                assertTrue(
                    tagsWithoutDescription.isEmpty(),
                    "$path содержит теги без описания: [${tagsWithoutDescription}] "
                )
            }
    }

    @Test
    @DisplayName("Все md узлы содержат только те теги у которых есть описание в $PATH_TO_TAGS_DESCRIPTION_MD ")
    fun checkCommonRuleWhatAllTagsFromMdHaveDescriptionTest() {
        val tagsEnums = getTagsEnums()
        getAllMDNodes()
            .forEach { path ->
                val notRecognisedTags = ParseUtil.mdParser.parse(Files.readString(path))
                    .getChildrenOfType(Tag::class)
                    .filter { it.tag.toString() != "TODO" }//TODO убрать
                    .map { it.openingMarker.toString() + it.tag.toString() }
                    .filter { mayBeTag -> !tagsEnums.any { it.tagOf(mayBeTag) != null } }

                assertTrue(notRecognisedTags.isEmpty(), "$path содержит неопознанные теги: [${notRecognisedTags}]")
            }

    }

    @Test
    @DisplayName("Все md узлы должны содержать в Yaml Front Matter 1 языковой тег")
    fun checkCommonRuleWhatYamlFrontMatterContainsLanguageTagTest() {
        getAllMDNodes()
            .forEach { path ->
                val tagsCnt = ParseUtil.mdParser.parse(Files.readString(path))
                    .getAllFrontMatterNodesFromFirstFMBlock("tags")
                    .map("#"::plus)
                    .filter { LanguageTag.tagOf(it) != null }

                assertEquals(
                    1,
                    tagsCnt.size,
                    "$path содержит ${tagsCnt.size} шт LanguageTag:[$tagsCnt]. Должен 1! "
                )
            }
    }


}

