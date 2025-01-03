package template

import com.vladsch.flexmark.ast.BulletListItem
import com.vladsch.flexmark.ast.OrderedListItem
import com.vladsch.flexmark.util.ast.Node
import java.util.regex.Pattern

class MDTListItemNode(
    parent: MDBaseNode? = null,
    node: Node? = null,
    optional: Boolean? = false,
    children: MutableList<MDBaseNode>? = mutableListOf(),
    charsRegex: String? = if (node == null) null else Pattern.quote(node.chars.toString().trim()),
    strictChildrenOrder: Boolean? = false,
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

    constructor(mdtNode: MDTListItemNode) : this(//copy constructor
        mdtNode.parent,
        mdtNode.node,
        mdtNode.optional,
        mdtNode.children?.map(::copyMDBaseNode)?.toMutableList(),
        mdtNode.charsRegex,
        mdtNode.strictChildrenOrder,
        mdtNode.specificNextNode,
        mdtNode.id,
        mdtNode.anySameNotTemplatedNode_BeforeAllowed,
        mdtNode.anySameNotTemplatedNode_AfterAllowed,
        mdtNode.nodeText,
        mdtNode.multiNodes
    )

    companion object : MDTemplateble {
        override fun getTemplatebleClasses(): Set<Class<*>> {
            return setOf(BulletListItem::class.java, OrderedListItem::class.java)
        }
    }
}