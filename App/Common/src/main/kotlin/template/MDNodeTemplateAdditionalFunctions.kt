package template

import com.vladsch.flexmark.ast.Heading
import com.vladsch.flexmark.ast.ListBlock
import com.vladsch.flexmark.ast.ListItem
import com.vladsch.flexmark.util.ast.Document
import com.vladsch.flexmark.util.ast.Node
import getAllFrontMatterNodesFromFirstFMBlock
import getChildrenOfType
import tags.StructRoleNodeTag
import java.util.*

/**
 * @throws RuntimeException if the document is invalid
 */
fun Document.validate(docTemplate: MDTDocumentNode) {

    docTemplate.prepareParentProperty(null)
    docTemplate.checkParentExists()

    docTemplate.prepareNodeProperty(this)
    docTemplate.checkNodeExists()

    docTemplate.checkNodeExists()

//    assert(
//        this.getAllFrontMatterNodesFromFirstFMBlock("tags")
//            .map("#"::plus)
//            .map(StructRoleNodeTag::tagOf)
//            .filter(Objects::nonNull)
//            .first()
//                == docTemplate.nodeType, { "StructRoleNodeTag не совпадает с шаблоном${docTemplate.nodeType}" }
//    )


    docTemplate.strictChildrenOrder

}

/**
 * Заполняем поле Node в шаблоне
 */
private fun MDTDocumentNode.prepareNodeProperty(node: Document): MDTDocumentNode {
    this.node = node
    this.children?.forEach { it.prepareNodeProperty(node) }

    return this
}

private fun MDBaseNode.prepareNodeProperty(node: Node): MDBaseNode {

//    this.getTemplatebleClasses
//    node.getChildrenOfType()

//    this.children?.forEach { it.prepareNodeProperty(it.parent!!.node ) }

    return this
}


fun MDBaseNode.prepareParentProperty(parent: MDBaseNode?): MDBaseNode {
    this.parent = parent
    this.children?.forEach {
        it.prepareParentProperty(this)
    }
    return this
}

private fun MDBaseNode.checkParentExists() {
    this.children?.forEach {
        assert(it.parent != null) { "Не указан родительский элемент" }
        it.checkParentExists()
    }
}

private fun MDBaseNode.checkNodeExists() {
    assert(this.node != null) { "Не указан node" }
    this.children?.forEach {
        assert(it.node != null) { "Не указан node" }
        it.checkNodeExists()
    }
}

/**
 * Используется для быстрого построения шаблона по новому документу.
 * Далее этот шаблон копируется в json файл-схему и правится руками до достижения нужного результата
 */
fun Document.toTemplate(): MDTDocumentNode {
    val nodeType = this.getAllFrontMatterNodesFromFirstFMBlock("tags")
        .map("#"::plus)
        .map(StructRoleNodeTag::tagOf)
        .filter(Objects::nonNull)
        .first() ?: throw RuntimeException("Файл не имеет структурного тега")

    return parseToTemplateRecursive(MDTDocumentNode(this, nodeType), this.children.toList()) as MDTDocumentNode
}

private fun parseToTemplateRecursive(parent: MDBaseNode, children: List<Node>): MDBaseNode {
    for (child in children) {
        if (child::class.java in templatableClasses) {
            parent.children?.add(parseToTemplateRecursive(createMDBaseNode(parent, child), child.children.toList()))
        } else {
            parseToTemplateRecursive(parent, child.children.toList())
        }
    }

    return parent
}

private fun createMDBaseNode(parent: MDBaseNode, node: Node): MDBaseNode {
    return when (node::class.java) {
        in MDTListNode.getTemplatebleClasses() -> MDTListNode(parent, node as ListBlock)
        in MDTListItemNode.getTemplatebleClasses() -> MDTListItemNode(parent, node as ListItem)
        in MDTLinkNode.getTemplatebleClasses() -> MDTLinkNode(parent, node)
        in MDTHeaderNode.getTemplatebleClasses() -> MDTHeaderNode(parent, node as Heading)
        else -> throw RuntimeException("Node: $node is not templatable")
    }
}

val templatableClasses = MDTListNode.getTemplatebleClasses() + MDTListItemNode.getTemplatebleClasses() +
        MDTLinkNode.getTemplatebleClasses() + MDTHeaderNode.getTemplatebleClasses()

