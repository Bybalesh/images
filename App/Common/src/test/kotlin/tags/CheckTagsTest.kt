package tags

import MCSystemFiles
import MDParserUtil
import PATH_TO_TAGS_DESCRIPTION_MD
import findChildrenByTypeAndMaybeName
import org.intellij.markdown.MarkdownElementTypes.ATX_2
import org.intellij.markdown.MarkdownElementTypes.ATX_3
import org.intellij.markdown.MarkdownElementTypes.ORDERED_LIST
import org.intellij.markdown.MarkdownElementTypes.UNORDERED_LIST
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals

class CheckTagsTest {

    @Test
    @DisplayName("RoleTag enum соответствует списку из $PATH_TO_TAGS_DESCRIPTION_MD и имеет соответствующую структуру")
    fun checkRoleTags() {
        val tagName = "## Роли в команде"
        val enumTags = RoleTag.entries
            .map(RoleTag::tag)
            .sorted()

        checkTag(tagName, enumTags)
    }

    @Test
    @DisplayName("LanguageTag enum соответствует списку из $PATH_TO_TAGS_DESCRIPTION_MD и имеет соответствующую структуру")
    fun checkLanguageTags() {
        val tagName = "## Языковые ([Коды языков](https://ru.wikipedia.org/wiki/Коды_языков))"
        val enumTags = LanguageTag.entries
            .map(LanguageTag::tag)
            .sorted()

        checkTag(tagName, enumTags)
    }

    @Test
    @DisplayName("StructRoleNodeTag enum соответствует списку из $PATH_TO_TAGS_DESCRIPTION_MD и имеет соответствующую структуру")
    fun checkStructRoleNodeTags() {
        val tagName = "## Структурные роли узлов"
        val enumTags = StructRoleNodeTag.entries
            .map(StructRoleNodeTag::tag)
            .sorted()

        checkTag(tagName, enumTags)
    }

    @Test
    @DisplayName("ProgLangTag enum соответствует списку из $PATH_TO_TAGS_DESCRIPTION_MD и имеет соответствующую структуру")
    fun checkProgLangTags() {
        val tagName = "## Языки программирования"
        val enumTags = ProgLangTag.entries
            .map(ProgLangTag::tag)
            .sorted()

        checkTag(tagName, enumTags)
    }

    @Test
    @DisplayName("EstimatingComplexityTag enum соответствует списку из $PATH_TO_TAGS_DESCRIPTION_MD и имеет соответствующую структуру")
    fun checkEstimatingComplexityTags() {
        val tagName = "## Сложность [[Описание хештегов#Структурные роли узлов|оценивающих узлов]]"
        val enumTags = EstimatingComplexityTag.entries
            .map(EstimatingComplexityTag::tag)
            .sorted()

        checkTag(tagName, enumTags)
    }

    @Test
    @DisplayName("CompetenceLVLTag enum соответствует списку из $PATH_TO_TAGS_DESCRIPTION_MD и имеет соответствующую структуру")
    fun checkCompetenceLVLTags() {
        val tagName = "## Уровень компетенций"
        val enumTags = CompetenceLVLTag.entries
            .map(CompetenceLVLTag::tag)
            .sorted()

        checkTag(tagName, enumTags)
    }

    private fun checkTag(tagName: String, enumTags: List<String>) {
        val rawMarkDown = MCSystemFiles.getTagsDescription()

        val rootNode = MDParserUtil.parse(rawMarkDown)

        val h2TagNode = rootNode.findChildrenByTypeAndMaybeName(ATX_2, rawMarkDown, tagName)
            .takeIf { it.isNotEmpty() }
            ?.first() ?: throw RuntimeException("$PATH_TO_TAGS_DESCRIPTION_MD не имеет описания для $tagName")

        h2TagNode.findChildrenByTypeAndMaybeName(ATX_3, rawMarkDown, DESCRIPTION_HEADER3)
            .takeIf { it.isNotEmpty() }
            ?: throw RuntimeException("$PATH_TO_TAGS_DESCRIPTION_MD не имеет раздела $DESCRIPTION_HEADER3 для $tagName")

        val h3RolesList = h2TagNode.findChildrenByTypeAndMaybeName(ATX_3, rawMarkDown, TAG_LIST_HEADER3)
            .takeIf { it.isNotEmpty() }
            ?.first()
            ?: throw RuntimeException("$PATH_TO_TAGS_DESCRIPTION_MD не имеет раздела $TAG_LIST_HEADER3 для $tagName")

        val tagsListMDNode =
            h3RolesList.findChildrenByTypeAndMaybeName(listOf(ORDERED_LIST, UNORDERED_LIST), rawMarkDown)
                .first()

        val stringHasOneWord = { str: String -> str.split("\\s+".toRegex()).count() == 1 }

        val tagsFromMD =
            TagUtil.expandAndClearTags(
                tagsListMDNode.findChildrenByTypeAndMaybeName(MarkdownTokenTypes.TEXT, rawMarkDown)
                    .map { it.getTextInNode(rawMarkDown).toString() }
                    .filter { it.startsWith("#").or(it.startsWith("/")) && stringHasOneWord.invoke(it) }
            ).sorted()

        enumTags.forEach { if (!tagsFromMD.contains(it)) System.err.println("MD has no $it") }
        tagsFromMD.forEach { if (!enumTags.contains(it)) System.err.println("Enum has no $it") }

        assertContentEquals(enumTags, tagsFromMD)
    }

    //    bb = StringBuilder()
// h3RolesList.findChildByTypeAndMaybeName(ORDERED_LIST, rawMDMC).map { it.getTextInNode(rawMDMC) }
    //printAllChild(rootNode,rawMDMC, StringBuilder(),"").toString()
    fun printAllChild(node: ASTNode, str: String, bb: StringBuilder, tabs: String): StringBuilder {
        bb.append(tabs)
        bb.append(node.type)
        bb.append(node.getTextInNode(str).toString())
        bb.append('\n')
        node.children.forEach { printAllChild(it, str, bb, tabs + "\t->") }
        return bb
    }


}