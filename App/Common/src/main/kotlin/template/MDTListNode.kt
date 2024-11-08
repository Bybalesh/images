package template

import com.vladsch.flexmark.ast.BulletList
import com.vladsch.flexmark.ast.OrderedList
import com.vladsch.flexmark.util.ast.Node
import kotlin.reflect.KClass

class MDTListNode(
    parent: MDBaseNode? = null,
    node: Node? = null,//ListBlock
    children: MutableList<MDBaseNode>? = mutableListOf(),
    strictChildrenOrder: Boolean = false,
    charsRegex: String? = null,
    specificNextNode: Set<Node> = emptySet(),
) : MDBaseNode(
    node,
    node?.chars?.contains('*'),
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
            return setOf(BulletList::class.java, OrderedList::class.java)
        }
    }
}


