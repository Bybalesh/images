package template

import ParseUtil
import com.fasterxml.jackson.module.kotlin.readValue
import com.vladsch.flexmark.util.ast.Document
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertTrue

private const val s = "strictChildOrder_allChildNonOptional_anyNodeBBANotAllowed-template.json"

class MDNodeTemplateTest {

    @Test
    fun generateTemplate() {
        val pathToMd = MDNodeTemplateTest::class.java.getClassLoader()
            .getResource("template/strictChildOrder_allChildNonOptional_anyNodeBBANotAllowed.md")

        val docMd: Document = ParseUtil.mdParser.parse(Files.readString(Path.of(pathToMd.toURI())))

        val template = docMd.toTemplate().prepareParentProperty(null)

        val json = ParseUtil.jsonMapper.writeValueAsString(template)

        val templateRR: MDTDocumentNode = ParseUtil.jsonMapper.readValue(json)

        println(ParseUtil.jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(template))
    }

    @Test
    @DisplayName(
        """
        strictChildrenOrder = true
        All children are NON optional
        anyOtherNodeBeforeChildrenAllowed = false
        anyOtherNodeBetweenChildrenAllowed = false
        anyOtherNodeAfterChildrenAllowed = false
    """
    )
    fun strictChildOrder_allChildNonOptional_anyNodeBBANotAllowed_OkTest() {
        val schemaFileName = "strictChildOrder_allChildNonOptional_anyNodeBBANotAllowed-template.json"
        val checkedMadoc = "strictChildOrder_allChildNonOptional_anyNodeBBANotAllowed.md"
        assertTrue(tryValidateBySchema(schemaFileName, checkedMadoc).isSuccess)
    }

    @Test
    @DisplayName(
        """
        strictChildrenOrder = true
        All children are NON optional
        anyOtherNodeBeforeChildrenAllowed = false
        anyOtherNodeBetweenChildrenAllowed = false
        anyOtherNodeAfterChildrenAllowed = false
    """
    )
    fun strictChildOrder_allChildNonOptional_anyNodeBBANotAllowed_FailTest() {
        val schemaFileName = "strictChildOrder_allChildNonOptional_anyNodeBBANotAllowed-template.json"
        val checkedMadoc = "(NON)strictChildOrder_allChildNonOptional_anyNodeBBANotAllowed.md"
        assertTrue(tryValidateBySchema(schemaFileName, checkedMadoc).isFailure)
    }

    private fun tryValidateBySchema(schemaFileName: String, chekedMdoc: String): Result<Unit> {
        val pathToTemplate = MDNodeTemplateTest::class.java.getClassLoader()
            .getResource("template/$schemaFileName")

        val template: MDTDocumentNode =
            ParseUtil.jsonMapper.readValue<MDTDocumentNode>(Files.readString(Path.of(pathToTemplate.toURI())))
                .prepareParentProperty(null) as MDTDocumentNode


        val pathToMd = MDNodeTemplateTest::class.java.getClassLoader()
            .getResource("template/$chekedMdoc")

        val docMd: Document = ParseUtil.mdParser.parse(Files.readString(Path.of(pathToMd.toURI())))

        //        println(ParseUtil.jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(docMd.toTemplate()))
        return runCatching { docMd.validate(template) }
            .onFailure { println(it) }
    }
}