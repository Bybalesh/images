package template

import com.vladsch.flexmark.ast.BulletListItem
import com.vladsch.flexmark.ast.OrderedListItem
import com.vladsch.flexmark.util.ast.Node
import kotlin.reflect.KClass

class MDTListItemNode(
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
    getTemplatebleClasses(),
    specificNextNode,
    parent,
) {
    companion object : MDTemplateble {
        override fun getTemplatebleClasses(): Set<Class<*>> {
            return setOf(BulletListItem::class.java, OrderedListItem::class.java)
        }
    }
}