package template

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.vladsch.flexmark.ast.Heading
import com.vladsch.flexmark.util.ast.Node
import java.util.regex.Pattern

@JsonInclude(JsonInclude.Include.NON_NULL)
class MDTHeaderNode(
    parent: MDBaseNode? = null,
    header: Node? = null,
    var level: Int? = if (header == null) null else (header as Heading).level,
    optional: Boolean? = header?.chars?.contains("*") ?: false,
    children: MutableList<MDBaseNode>? = mutableListOf(),
    strictChildrenOrder: Boolean = true,
    charsRegex: String? = if (header == null) null else Pattern.quote(header?.chars.toString().trim()),
    specificNextNode: Set<Node> = emptySet(),
    id: String? = null,
) : MDBaseNode(
    id,
    header,
    optional,
    charsRegex,
    children,
    strictChildrenOrder,
    false,
    false,
    getTemplatebleClasses(),
    specificNextNode,
    parent,
) {
    companion object : MDTemplateble {
        @JsonIgnore
        override fun getTemplatebleClasses(): Set<Class<*>> {
            return setOf(Heading::class.java)
        }
    }
}
