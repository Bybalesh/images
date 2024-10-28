import CMSystemFiles.getTagsDescription
import TagUtil.getTagsEnums
import com.vladsch.flexmark.ast.*
import com.vladsch.flexmark.ext.tag.Tag
import com.vladsch.flexmark.util.ast.Document
import com.vladsch.flexmark.util.ast.Node
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import tags.*
import tags.TagUtil.expandAndClearTags
import kotlin.test.assertContentEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@DisplayName("Проверяем наличие описания для тегов и согласованность с кодом")
class CheckTagsMandatoryTest {

    @Test
    @DisplayName("Весь список тегов из $PATH_TO_TAGS_DESCRIPTION_MD задекларирован в App/Common/src/main/kotlin/tags")
    fun checkAllTagsDeclared() {

        val tagsEnums = getTagsEnums()

        assertFalse(tagsEnums.isEmpty(), "Список тегов не должен быть пустым!")

        val listEnumsNames = tagsEnums.map(IMDTag<*>::tagsName)

        val setEnumsNames = listEnumsNames.toSet()

        assertEquals(listEnumsNames.toString(), setEnumsNames.toString(), "Обнаружены дубли")

        tagsEnums.forEach {
            checkTag(it.tagsName(), it.tagsAsStr().sorted(), it)
        }
    }

    private fun checkTag(tagName: String, enumTags: List<String>, initEnum: IMDTag<*>) {

        val tagsDescriptionDocument = ParseUtil.mdParser.parse(getTagsDescription())

        val h2TagNode = checkTagExists(tagsDescriptionDocument, tagName)

        checkDescription(h2TagNode, tagName)

        checkMandatory(h2TagNode, tagName, initEnum)

        val h3TagsList = checkListExists(h2TagNode, tagName)

        checkTagsListAndEnumEquals(h3TagsList, enumTags)
    }

    private fun checkMandatory(
        h2TagNode: List<Heading>,
        tagName: String,
        initEnum: IMDTag<*>,
    ) {
        val h3TagMandatory =
            h2TagNode.first()
                .getChildrenOfType(Heading::class,
                    { h ->
                        h.level == 3 && (h.text.equals(TAG_REQUIRED_HEADER3) || h.text.equals(
                            TAG_OPTIONAL_HEADER3
                        ))
                    })
        if (h3TagMandatory.isEmpty())
            throw RuntimeException("$PATH_TO_TAGS_DESCRIPTION_MD не имеет раздела $TAG_REQUIRED_HEADER3 || $TAG_OPTIONAL_HEADER3 для $tagName")
        if (h3TagMandatory.size > 1)
            throw RuntimeException(
                "$PATH_TO_TAGS_DESCRIPTION_MD имеет уберите разделы с похожим началом названия: [${h3TagMandatory.map { it.chars }}] "
            )

        assertTrue(
            h3TagMandatory.first().text.equals(if (initEnum.isOptional()) TAG_OPTIONAL_HEADER3 else TAG_REQUIRED_HEADER3),
            "Значение обязательности не совпадает для [${initEnum.tagsName()}]"
        )
    }

    private fun checkTagsListAndEnumEquals(
        h3TagsList: List<Heading>,
        enumTags: List<String>,
    ) {
        val stringHasOneWord = { str: String -> str.split("\\s+".toRegex()).count() == 1 }

        val tagsFromMD =
            expandAndClearTags(
                h3TagsList.first()
                    .getChildrenOfType<ListBlock>(listOf(OrderedList::class, BulletList::class))
                    .first()
                    .getChildrenOfType<Node>(listOf(Text::class, Tag::class))
                    .map(Node::getChars).map { it.toString() }
                    .filter { it.startsWith("#").or(it.startsWith("/")) && stringHasOneWord.invoke(it) }
            ).sorted()

        enumTags.forEach { if (!tagsFromMD.contains(it)) System.err.println("MD has no $it") }
        tagsFromMD.forEach { if (!enumTags.contains(it)) System.err.println("Enum has no $it") }

        assertContentEquals(enumTags, tagsFromMD)
    }

    private fun checkListExists(
        h2TagNode: List<Heading>,
        tagName: String,
    ): List<Heading> {
        val h3TagsList =
            h2TagNode.first()
                .getChildrenOfType(Heading::class,
                    { h -> h.level == 3 && h.text.equals(TAG_LIST_HEADER3) })

        if (h3TagsList.isEmpty())
            throw RuntimeException("$PATH_TO_TAGS_DESCRIPTION_MD не имеет раздела $TAG_LIST_HEADER3 для $tagName")
        if (h3TagsList.size > 1)
            throw RuntimeException(
                "$PATH_TO_TAGS_DESCRIPTION_MD имеет уберите разделы с похожим началом названия: [${h3TagsList.map { it.chars }}] "
            )
        return h3TagsList
    }

    private fun checkTagExists(
        tagsDescriptionDocument: Document,
        tagName: String,
    ): List<Heading> {
        val h2TagNode = tagsDescriptionDocument.getChildrenOfType(Heading::class,
            { h -> h.level == 2 && h.text.startsWith(tagName) })

        if (h2TagNode.isEmpty())
            throw RuntimeException("$PATH_TO_TAGS_DESCRIPTION_MD не имеет описания для тега [$tagName]")
        if (h2TagNode.size > 1)
            throw RuntimeException("$PATH_TO_TAGS_DESCRIPTION_MD имеет headers с одинаковыми именами начинающимися с $tagName :[${h2TagNode.map { it.chars }}]")
        return h2TagNode
    }

    private fun checkDescription(h2TagNode: List<Heading>, tagName: String) {
        val h3TagDescription =
            h2TagNode.first()
                .getChildrenOfType(Heading::class,
                    { h -> h.level == 3 && h.text.equals(DESCRIPTION_HEADER3) })

        if (h3TagDescription.isEmpty())
            throw RuntimeException("$PATH_TO_TAGS_DESCRIPTION_MD не имеет раздела $DESCRIPTION_HEADER3 для $tagName")
        if (h3TagDescription.size > 1)
            throw RuntimeException(
                "$PATH_TO_TAGS_DESCRIPTION_MD имеет разделы с похожим началом названия: [${h3TagDescription.map { it.chars }}] "
            )
    }

}