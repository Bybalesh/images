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
 * TODO написать хорошее сообщение в каждой ошибке с путем к ноде, как это делает Jackson
 */
fun Document.validate(docTemplate: MDTDocumentNode) {

    docTemplate.prepareParentProperty(null)
    docTemplate.checkParentsExists()

    docTemplate.prepareNodeProperty(this)
    docTemplate.checkNodeExistsAndUniq()

    docTemplate.checkChildrenOrder_beforeAfterSameNotTemplatedAllowed()

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

/**
 * Здесь будет проблема, если одна нода схемы будет универсальна и подойдёт нескольким нодам документа
 * Тогда другой ноде схемы может не хватить водходящей ноды документа.
 * Для схем ноды не должны повторяться ноды документа.
 */
private fun MDBaseNode.prepareNodeProperty(node: Node): MDBaseNode {

    val nodes = node.getChildrenOfType<Node>(this.templatableClasses!!.map(Class<*>::kotlin),
        { it.chars.equals(this.charsRegex) })//TODO для начала пусть точное будет совпадение
    this.node = nodes.first()
    assert(this.node != null) { "Не найдено ноды для шаблона" }

    this.children?.forEach { it.prepareNodeProperty(this.node!!) }
    return this
}


fun MDBaseNode.prepareParentProperty(parent: MDBaseNode?): MDBaseNode {
    this.parent = parent
    this.children?.forEach {
        it.prepareParentProperty(this)
    }
    return this
}

private fun MDBaseNode.checkParentsExists() {
    this.children?.forEach {
        assert(it.parent != null) { "Не указан родительский элемент" }
        it.checkParentsExists()
    }
}

private fun MDBaseNode.checkNodeExistsAndUniq(uniqNodes: MutableSet<Node> = mutableSetOf()) {
    assert(this.optional ?: false || this.node != null) { "Не указан node" }
    this.children?.forEach {
        assert(this.optional ?: false || it.node != null) { "Не указан node" }
        assert(uniqNodes.add(it.node!!)) { "Не уникальный элемент" }

        it.checkNodeExistsAndUniq(uniqNodes)
    }
}

private fun MDBaseNode.checkChildrenOrder_beforeAfterSameNotTemplatedAllowed() {
    var prevNode: MDBaseNode? = null
    var nextNode: MDBaseNode? = null
    this.children?.forEach { currentNode ->

        nextNode = this.children?.getNextBy(currentNode)

        if (this.strictChildrenOrder ?: false && prevNode != null) {
            assert(prevNode!!.node!!.startOffset < currentNode.node!!.startOffset) { "Не указан родительский элемент" }
        }

        if (currentNode.anySameNotTemplatedNode_BeforeAllowed != true) {
            val incorrectNode = currentNode.node?.findSuitableNodeInPreviousOnesUntil(
                currentNode.templatableClasses!!.toList(),
                prevNode?.node
            )
            assert(incorrectNode == null) { "Нарушено правило currentNode.anySameNotTemplatedNode_BeforeAllowed = ${currentNode.anySameNotTemplatedNode_BeforeAllowed}" }
        }

        if (currentNode.anySameNotTemplatedNode_AfterAllowed != true) {
            val incorrectNode = currentNode.node?.findSuitableNodeInNextOnesUntil(
                currentNode.templatableClasses!!.toList(),
                nextNode?.node//TODO Запустить тесты и проверить
            )
            assert(incorrectNode == null) { "Нарушено правило currentNode.anySameNotTemplatedNode_AfterAllowed = ${currentNode.anySameNotTemplatedNode_AfterAllowed}" }
        }

        currentNode.checkChildrenOrder_beforeAfterSameNotTemplatedAllowed()

        prevNode = currentNode
    }
}

fun <E> Collection<E>.getPreviousBy(element: E): E? {
    return this.elementAtOrNull(this.indexOf(element) - 1)
}

fun <E> Collection<E>.getNextBy(element: E): E? {
    return this.elementAtOrNull(this.indexOf(element) + 1)
}


private fun Node.findSuitableNodeInPreviousOnesUntil(targetClasses: List<Class<*>>, node: Node?): Node? {
    var prevNode: Node? = this.previous
    while (prevNode != node && prevNode != null) {
        if (prevNode::class.java in targetClasses)
            return prevNode
        prevNode = prevNode.previous
    }
    return null
}

private fun Node.findSuitableNodeInNextOnesUntil(targetClasses: List<Class<*>>, node: Node?): Node? {
    var nextNode: Node? = this.next
    while (nextNode != node && nextNode != null) {
        if (nextNode::class.java in targetClasses)
            return nextNode
        nextNode = nextNode.next
    }
    return null
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

