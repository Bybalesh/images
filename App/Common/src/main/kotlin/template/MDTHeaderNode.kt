package template

import com.fasterxml.jackson.annotation.JsonIgnore
import com.vladsch.flexmark.ast.Heading
import com.vladsch.flexmark.util.ast.Node
import kotlin.reflect.KClass

class MDTHeaderNode(
    parent: MDBaseNode? = null,
    header: Node? = null,
    var level: Int? = if (header == null) null else (header as Heading).level,
    optional: Boolean? = header?.chars?.contains("*") ?: false,
    children: MutableList<MDBaseNode>? = mutableListOf(),
    strictChildrenOrder: Boolean = true,
    charsRegex: String? = header?.chars.toString(),
    specificNextNode: Set<Node> = emptySet(),
) : MDBaseNode(
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
