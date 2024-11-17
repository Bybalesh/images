package template

import com.vladsch.flexmark.ast.Link
import com.vladsch.flexmark.ext.wikilink.WikiLink
import com.vladsch.flexmark.util.ast.Node

class MDTLinkNode(//TODO помоему этот класс не пригодится в шаблонах т.к. проще добавить regex, чем проверять наличие ссылки через шаблон с этим эллементом
    parent: MDBaseNode? = null,
    node: Node? = null,
    optional: Boolean? = false,
    children: MutableList<MDBaseNode>? = mutableListOf(),
    strictChildrenOrder: Boolean? = false,
    charsRegex: String? = null,
    specificNextNode: Set<Node>? = emptySet(),
    id: String? = null,
    anySameNotTemplatedNode_BeforeAllowed: Boolean? = false,
    anySameNotTemplatedNode_AfterAllowed: Boolean? = false,
    nodeText: String? = null,
    multiNodes: Boolean? = null
) : MDBaseNode(
    id,
    node,
    optional,
    charsRegex,
    children,
    strictChildrenOrder,
    anySameNotTemplatedNode_BeforeAllowed,
    anySameNotTemplatedNode_AfterAllowed,
    getTemplatebleClasses(),
    specificNextNode,
    parent,
    nodeText,
    multiNodes
) {
    constructor(mdtNode: MDTLinkNode) : this(//copy constructor
        mdtNode.parent,
        mdtNode.node,
        mdtNode.optional,
        mdtNode.children?.map(::copyMDBaseNode)?.toMutableList(),
        mdtNode.strictChildrenOrder,
        mdtNode.charsRegex,
        mdtNode.specificNextNode,
        mdtNode.id,
        mdtNode.anySameNotTemplatedNode_BeforeAllowed,
        mdtNode.anySameNotTemplatedNode_AfterAllowed,
        mdtNode.nodeText,
        mdtNode.multiNodes
    )

    companion object : MDTemplateble {
        override fun getTemplatebleClasses(): Set<Class<*>> {
            return setOf(WikiLink::class.java, Link::class.java)
        }
    }
}
