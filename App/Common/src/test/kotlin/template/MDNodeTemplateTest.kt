package template

import ParseUtil
import com.fasterxml.jackson.module.kotlin.readValue
import com.vladsch.flexmark.util.ast.Document
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.nio.file.Files
import java.nio.file.Path

class MDNodeTemplateTest {

    @Test
    fun generateTemplate() {
        val pathToMd = MDNodeTemplateTest::class.java.getClassLoader()
            .getResource("template/optional.md")

        val docMd: Document = ParseUtil.mdParser.parse(Files.readString(Path.of(pathToMd.toURI())))

        val template = docMd.toTemplate().prepareParentProperty(null)

        val json = ParseUtil.jsonMapper.writeValueAsString(template)

        val templateRR: MDTDocumentNode = ParseUtil.jsonMapper.readValue(json)

        println(ParseUtil.jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(template))
    }

    @Test
    fun optionalTest() {
        val pathToTemplate = MDNodeTemplateTest::class.java.getClassLoader()
            .getResource("template/optional-template.json")

        val template: MDTDocumentNode =
            ParseUtil.jsonMapper.readValue<MDTDocumentNode>(Files.readString(Path.of(pathToTemplate.toURI())))
                .prepareParentProperty(null) as MDTDocumentNode

        val pathToMd = MDNodeTemplateTest::class.java.getClassLoader()
            .getResource("template/optional.md")

        val docMd: Document = ParseUtil.mdParser.parse(Files.readString(Path.of(pathToMd.toURI())))

        assertDoesNotThrow { docMd.validate(template) }

//        println( ParseUtil.jsonMapper.writeValueAsString(docMd.toTemplate()))
    }
}