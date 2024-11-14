package template

import com.vladsch.flexmark.util.ast.Document
import com.vladsch.flexmark.util.ast.Node
import tags.StructRoleNodeTag

class MDTDocumentNode(
    node: Node? = null,//Document,
    var nodeType: StructRoleNodeTag,
    children: MutableList<MDBaseNode>? = mutableListOf(),
) : MDBaseNode(
    node,
    null,
    null,
    children,
    true,
    false,
    false,
    getTemplatebleClasses(),
) {

    companion object : MDTemplateble {
        override fun getTemplatebleClasses(): Set<Class<*>> {
            return setOf(Document::class.java)
        }
    }
}
