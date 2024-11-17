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
    strictChildrenOrder: Boolean? = true,
    charsRegex: String? = if (header == null) null else Pattern.quote(header?.chars.toString().trim()),
    specificNextNode: Set<Node>? = emptySet(),
    id: String? = null,
    anySameNotTemplatedNode_BeforeAllowed: Boolean? = false,
    anySameNotTemplatedNode_AfterAllowed: Boolean? = false,
    nodeText: String? = null,
    multiNodes: Boolean? = null
) : MDBaseNode(
    id,
    header,
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

    constructor(mdtNode: MDTHeaderNode) : this(//copy constructor
        mdtNode.parent,
        mdtNode.node,
        mdtNode.level,
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
        @JsonIgnore
        override fun getTemplatebleClasses(): Set<Class<*>> {
            return setOf(Heading::class.java)
        }
    }
}
