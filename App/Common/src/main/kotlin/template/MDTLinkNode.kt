package template

import com.vladsch.flexmark.ast.Link
import com.vladsch.flexmark.ext.wikilink.WikiLink
import com.vladsch.flexmark.util.ast.Node
import kotlin.reflect.KClass

class MDTLinkNode(//TODO помоему этот класс не пригодится в шаблонах т.к. проще добавить regex, чем проверять наличие ссылки через шаблон с этим эллементом
    parent: MDBaseNode? = null,
    node: Node? = null,
    optional: Boolean? = false,
    children: MutableList<MDBaseNode>? = mutableListOf(),
    strictChildrenOrder: Boolean = false,
    charsRegex: String? = null,
    specificNextNode: Set<Node> = emptySet(),
    id: String? = null,
) : MDBaseNode(
    id,
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
            return setOf(WikiLink::class.java, Link::class.java)
        }
    }
}
