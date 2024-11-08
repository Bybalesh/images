package template

import com.vladsch.flexmark.ast.Link
import com.vladsch.flexmark.ext.wikilink.WikiLink
import com.vladsch.flexmark.util.ast.Node
import kotlin.reflect.KClass

class MDTLinkNode(
    parent: MDBaseNode? = null,
    node: Node? = null,
    optional: Boolean? = false,
    children: MutableList<MDBaseNode>? = mutableListOf(),
    strictChildrenOrder: Boolean = false,
    charsRegex: String? = null,
    specificNextNode: Set<Node> = emptySet(),
) : MDBaseNode(
    node,
    optional,
    charsRegex,
    children,
    strictChildrenOrder,
    false,
    false,
    false,
    getTemplatebleClasses(),
    specificNextNode,
    parent,
) {
    companion object : MDTemplateble {
        override fun getTemplatebleClasses(): Set<Class<*>> {
            return setOf(WikiLink::class.java, Link::class.java)
        }
    }
}
