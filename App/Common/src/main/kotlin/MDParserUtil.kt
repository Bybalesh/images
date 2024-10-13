import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser

object MDParserUtil {

    private val flavour = CommonMarkFlavourDescriptor()

    fun parse(text: String): ASTNode {
        return MarkdownParser(flavour).buildMarkdownTreeFromString(text)
    }
}