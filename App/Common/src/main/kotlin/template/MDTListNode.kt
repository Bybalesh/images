package template

import com.vladsch.flexmark.ast.BulletList
import com.vladsch.flexmark.ast.OrderedList
import com.vladsch.flexmark.util.ast.Node

class MDTListNode(
    parent: MDBaseNode? = null,
    node: Node? = null,//ListBlock
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
    constructor(mdtNode: MDTListNode) : this(//copy constructor
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
            return setOf(BulletList::class.java, OrderedList::class.java)
        }
    }
}


