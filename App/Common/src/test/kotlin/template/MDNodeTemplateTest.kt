package template

import ParseUtil
import com.fasterxml.jackson.module.kotlin.readValue
import com.vladsch.flexmark.util.ast.Document
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertContains
import kotlin.test.assertTrue

class MDNodeTemplateTest {

    @Test
    @DisplayName("Во время генерации шаблона MD документа не должно произойти ошибок")
    fun generateTemplate() {
        val pathToMd = MDNodeTemplateTest::class.java.getClassLoader()
            .getResource("template/Non optional and right seq.md")

        val docMd: Document = ParseUtil.mdParser.parse(Files.readString(Path.of(pathToMd.toURI())))

        val template = docMd.toTemplate().prepareParentProperty(null)

        val json = ParseUtil.jsonMapper.writeValueAsString(template)

        val templateRR: MDTDocumentNode = ParseUtil.jsonMapper.readValue(json)

        println(ParseUtil.jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(template))
    }

    @Test
    @DisplayName(
        """
        Проверяем работу валидатора для шаблона со строгим порядком дочерних узлов
        и обязательным наличием всех шаблонизируемых узлов.
        Здесь MD документ должен полностью соответствовать шаблону.
    """
    )
    fun strictChildOrder_allChildNonOptional_anyNodeBBANotAllowed_OkTest() {
        val schemaFileName = "Non optional and right seq TEMPLATE.json"
        val checkedMadoc = "Non optional and right seq.md"
        assertTrue(tryValidateBySchema(schemaFileName, checkedMadoc).isSuccess)
    }

    @Test
    @DisplayName(
        """
        Проверяем работу валидатора для шаблона со строгим порядком дочерних узлов
        и обязательным наличием всех шаблонизируемых узлов.
        заголовки ### Не опциональный header1.2.2 и ### Не опциональный header1.2.1 перепутаны местами
    """
    )
    fun strictChildOrder_allChildNonOptional_anyNodeBBNotAllowed_v1FailTest() {
        val schemaFileName = "Non optional and right seq TEMPLATE.json"
        val checkedMadoc = "Non optional and wrong seq v1.md"
        assertTrue(tryValidateBySchema(schemaFileName, checkedMadoc)
            .onFailure {
                assertContains(
                    it.message.toString(),
                    "Согласно шаблону MD узлы [### Не опциональный header1.2.2] и [### Не Опциональный header1.2.1] должны поменяться местами."
                )
            }
            .isFailure)
    }

    @Test
    @DisplayName(
        """
        Проверяем работу валидатора для шаблона со строгим порядком дочерних узлов
        и обязательным наличием всех шаблонизируемых узлов.
        заголовки # Не опциональный header3: и # Не Опциональный header2 перепутаны местами
    """
    )
    fun strictChildOrder_allChildNonOptional_anyNodeBBNotAllowed_v2FailTest() {
        val schemaFileName = "Non optional and right seq TEMPLATE.json"
        val checkedMadoc = "Non optional and wrong seq v2.md"
        assertTrue(tryValidateBySchema(schemaFileName, checkedMadoc)
            .onFailure {
                assertContains(
                    it.message.toString(),
                    "Согласно шаблону MD узлы [# Не опциональный header3:] и [# Не опциональный header2] должны поменяться местами."
                )
            }
            .isFailure)
    }

    @Test
    @DisplayName(
        """
        Проверяем работу валидатора для шаблона со строгим порядком дочерних узлов
        и обязательным наличием всех шаблонизируемых узлов.
        заголовки ## Не Опциональный header1.2] и [## Не опциональный header1.1 перепутаны местами
    """
    )
    fun strictChildOrder_allChildNonOptional_anyNodeBBNotAllowed_v3FailTest() {
        val schemaFileName = "Non optional and right seq TEMPLATE.json"
        val checkedMadoc = "Non optional and wrong seq v3.md"
        assertTrue(tryValidateBySchema(schemaFileName, checkedMadoc)
            .onFailure {
                assertContains(
                    it.message.toString(),
                    "Согласно шаблону MD узлы [## Не Опциональный header1.2] и [## Не опциональный header1.1] должны поменяться местами."
                )
            }
            .isFailure)
    }

    @Test
    @DisplayName(
        """
        Проверяем работу валидатора для шаблона без строгого порядком дочерних узлов
        и обязательным наличием всех шаблонизируемых узлов.
    """
    )
    fun strictChildOrder_allChildNonOptional_anyNodeBBNotAllowed_v1OkTest() {
        val schemaFileName = "Non optional and any seq TEMPLATE.json"
        val checkedMadoc = "Non optional and wrong seq v1.md"
        assertTrue(tryValidateBySchema(schemaFileName, checkedMadoc).isSuccess)
    }

    @Test
    @DisplayName(
        """
        Проверяем работу валидатора для шаблона без строгого порядка дочерних узлов
        и обязательным наличием всех шаблонизируемых узлов.
    """
    )
    fun strictChildOrder_allChildNonOptional_anyNodeBBNotAllowed_v2OkTest() {
        val schemaFileName = "Non optional and any seq TEMPLATE.json"
        val checkedMadoc = "Non optional and wrong seq v2.md"
        assertTrue(tryValidateBySchema(schemaFileName, checkedMadoc)
            .onFailure {
                assertContains(
                    it.message.toString(),
                    "не может содержать следующие узлы без шаблона:### Не Опциональный header1.2.0"
                )
                assertContains(
                    it.message.toString(),
                    "не может содержать следующие узлы без шаблона:### Не опциональный header1.2.3"
                )

            }
            .isFailure)
    }


    @Test
    @DisplayName(
        """
        Проверяем работу валидатора для шаблона со строгим порядком дочерних узлов
        и не обязательным наличием первого родительского узла.
        Опциональный header был удалён, но так как он опциональный, то это не должно было повлиять на проверку наличия вложенных в него узлов
    """
    )
    fun strictChildOrder_nonAllChildNonOptional_anyNodeBBNotAllowed_OkTest() {
        val schemaFileName = "Optional distant parent v1 TEMPLATE.json"
        val checkedMadoc = "Optional distant parent v1.md"
        assertTrue(
            tryValidateBySchema(schemaFileName, checkedMadoc)
                .isSuccess
        )
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

        return runCatching { docMd.validate(template) }
            .onFailure { println(it) }
    }
}