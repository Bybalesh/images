package template

import com.vladsch.flexmark.ast.BulletListItem
import com.vladsch.flexmark.ast.OrderedListItem
import com.vladsch.flexmark.util.ast.Node
import java.util.regex.Pattern

class MDTListItemNode(
    parent: MDBaseNode? = null,
    node: Node? = null,
    children: MutableList<MDBaseNode>? = mutableListOf(),
    strictChildrenOrder: Boolean = false,
    specificNextNode: Set<Node> = emptySet(),
    id: String? = null,
) : MDBaseNode(
    id,
    node,
    true,
    if (node == null) null else Pattern.quote(node?.chars.toString().trim()),
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