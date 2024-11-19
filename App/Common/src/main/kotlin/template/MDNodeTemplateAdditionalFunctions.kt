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

typealias ErrorMessage = String

/**
 * @throws RuntimeException if the document is invalid
 * TODO написать хорошее сообщение в каждой ошибке с путем к ноде, как это делает Jackson
 * + выводить все ошибки, а не только первую
 */
fun Document.validate(docTemplate: MDTDocumentNode) {

    val errorMessages = mutableListOf<ErrorMessage>()
    try {
        docTemplate.prepareParentProperty(null)
        docTemplate.checkParentsExists()

        docTemplate.prepareNodeProperty(this, errorMessages)

        if (errorMessages.isEmpty())
            docTemplate.checkNodeExistsAndUniq(errorMessages)

        if (errorMessages.isEmpty())
            docTemplate.checkChildrenOrder(errorMessages)

        if (errorMessages.isEmpty())
            docTemplate.checkAnySameAllowedBeforeAfter(errorMessages, getAllNodes(docTemplate))

        assert(errorMessages.isEmpty()) {
            errorMessages.joinToString("\n")
//            + TODO раскомментировать в случае непонимания работы схемы
//                    """
//                    Effective template with current MD:
//                    $docTemplate
//                """
        }
    } catch (e: Exception) {
        throw RuntimeException("Не предвиденное исключение! Обратитесь к разработчикам.", e)
    }
}

/**
 * Заполняем поле Node в шаблоне
 */
private fun MDTDocumentNode.prepareNodeProperty(
    node: Document,
    errorMessages: MutableList<ErrorMessage> = mutableListOf()
): List<ErrorMessage> {
    this.node = node
    val childrenIt = this.children?.listIterator()
    if (childrenIt != null)
        for (child in childrenIt) {
            child.prepareNodeProperty(node, errorMessages, childrenIt)
        }
    return errorMessages
}

/**
 * Здесь будет проблема, если одна нода схемы будет универсальна и подойдёт нескольким нодам документа
 * Тогда другой ноде схемы может не хватить водходящей ноды документа.
 * Для схем ноды не должны повторяться ноды документа.
 */
private fun MDBaseNode.prepareNodeProperty(
    node: Node,
    errorMessages: MutableList<ErrorMessage>,
    _childrenIt: MutableListIterator<MDBaseNode>
) {
    val nodes = node.getChildrenOfType<Node>(
        this.templatableClasses!!.map(Class<*>::kotlin),
        {
            if (this.charsRegex == null && this.optional != true) {
                return@getChildrenOfType this.templatableClasses!!.contains(it::class.java)
            } else {
                return@getChildrenOfType this.charsRegex!!.toRegex(RegexOption.IGNORE_CASE).matches(it.chars.trim())
            }
        },
    )


    if (nodes.size > 1 && this.multiNodes == true) {
        this.node = nodes.first()
        val cpIndex: Int = this.parent!!.children!!.indexOf(this)
        nodes.drop(1).forEachIndexed { index, matchedNode ->
            val multiNode = copyMDBaseNode(this)
            multiNode.node = matchedNode
            val childrenIt = multiNode.children?.listIterator()
            if (childrenIt != null)
                for (child in childrenIt) {
                    child.prepareNodeProperty(multiNode.node!!, errorMessages, childrenIt)
                }
            multiNode.id = "${multiNode.id}(copy$index)"//TODO переделать для детей тоже нужно
            _childrenIt.add(multiNode)
        }
    } else {
        this.node = nodes.firstOrNull()
    }

    this.nodeText = this.node?.chars?.toString() ?: "empty"
    if (this.optional != true && this.node == null && this.charsRegex != null)
        errorMessages.add("Ошибка сразу после ${this.parent?.children?.getPreviousBy(this)?.node?.endLineNumber} строки MD документа. Не найдено MD ноды для узла шаблона с id =${this.id} . Его regexp: [${this.charsRegex}]")

    if (this.node != null) {
        val childrenIt = this.children?.listIterator()
        if (childrenIt != null)
            for (child in childrenIt) {
                child.prepareNodeProperty(this.node!!, errorMessages, childrenIt)
            }
    }
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
        assert(it.parent != null) { "Не указан родительский элемент. Обратитесь к разработчикам." }
        it.checkParentsExists()
    }
}

private fun MDTDocumentNode.checkNodeExistsAndUniq(
    errorMessages: MutableList<ErrorMessage> = mutableListOf(),
    uniqNodes: MutableSet<Node> = mutableSetOf()
) {
    if (this.optional != true && this.node == null)
        errorMessages.add("Ошибка сразу после ${this.parent?.children?.getPreviousBy(this)?.node?.endLineNumber ?: this.node?.endLineNumber}} строки MD документа. Не удалось найти MD node для узла шаблона с id =${this.id} и charsRegex={${this.charsRegex}}")
    (this as MDBaseNode).checkNodeExistsAndUniq(uniqNodes, errorMessages)
}

private fun MDBaseNode.checkNodeExistsAndUniq(
    uniqNodes: MutableSet<Node> = mutableSetOf(),
    errorMessages: MutableList<ErrorMessage> = mutableListOf()
) {
    this.children?.forEach loop@{

        if (it.optional != true && it.node == null) {
            errorMessages.add("Ошибка сразу после ${this.children?.getPreviousBy(it)?.node?.endLineNumber ?: this.node?.endLineNumber} строки MD документа. Не удалось найти MD node для узла шаблона с id =${it.id}  и charsRegex={${it.charsRegex}}")
            return@loop
        }

        if (it.node != null) {
            if (!uniqNodes.add(it.node!!))
                errorMessages.add(
                    """
                Ошибка на ${it.node?.lineNumber} строке MD документа.
                Одна MD node{${it.node?.chars}} соответствует нескольким узлам из шаблона id= ${this.id}.
                Определите название узла в шаблоне более индивидуально. Сейчас:{${it.charsRegex}}"""
                )

            it.checkNodeExistsAndUniq(uniqNodes, errorMessages)
        }
    }
}

private fun MDBaseNode.checkChildrenOrder(
    errorMessages: MutableList<ErrorMessage> = mutableListOf()
) {
    var prevTemplateWithNode: MDBaseNode? = null
    this.children
        ?.filter { it.node != null }
        ?.forEach { currentNode ->

            // Проверяем правило очерёдности дочерних узлов
            if (this.strictChildrenOrder == true && prevTemplateWithNode?.node != null) {
                if (prevTemplateWithNode!!.node!!.startOffset > currentNode.node!!.startOffset) {
                    val shouldBeBefore =
                        currentNode.node!!.chars
                    val shouldBeAfter =
                        prevTemplateWithNode!!.node!!.chars
                    errorMessages.add(
                        """
                    Согласно шаблону MD узлы [${shouldBeBefore}] и [${shouldBeAfter}] должны поменяться местами.
                    Т.к. задан строгий порядок дочерних узлов и для узлов с id1 = ${currentNode.id} и id2 = ${prevTemplateWithNode!!.id}
                    Следующие описания нарушения правил могут не иметь смысла. 
                    """
                    )
                }
            }

            prevTemplateWithNode = currentNode
            currentNode.checkChildrenOrder(errorMessages)
        }
}

private fun MDBaseNode.checkAnySameAllowedBeforeAfter(
    errorMessages: MutableList<ErrorMessage> = mutableListOf(),
    allTemplatedNodes: Set<Node>
) {
    this.children
        ?.filter { it.node != null }
        ?.forEach { mdtNode ->

            // Проверяем правило наличия других схожих не шаблонных узлов до текущего, если они запрещены
            if (mdtNode.anySameNotTemplatedNode_BeforeAllowed != true) {
                mdtNode.node!!.accumulatePreviousNodeUntilFindTemplatedOrNull(allTemplatedNodes)
                    .filter { mdtNode.templatableClasses!!.contains(it::class.java) }
                    .forEach {
                        errorMessages.add(
                            """
                    Нарушено правило currentNode.anySameNotTemplatedNode_BeforeAllowed = false для узла шаблона c id= ${mdtNode.id}.
                    MD не может содержать следующие узлы без шаблона:${it.chars}
                    """
                        )
                    }
            }

            // Проверяем правило наличия других схожих не шаблонных узлов до текущего, если они запрещены
            if (mdtNode.anySameNotTemplatedNode_AfterAllowed != true) {
                mdtNode.node!!.accumulateNextNodeUntilFindTemplatedOrNull(allTemplatedNodes)
                    .filter { mdtNode.templatableClasses!!.contains(it::class.java) }
                    .forEach {
                        errorMessages.add(
                            """
                    Нарушено правило currentNode.anySameNotTemplatedNode_AfterAllowed = false для узла шаблона c id= ${mdtNode.id}.
                    MD не может содержать следующие узлы без шаблона:${it.chars}
                    """
                        )
                    }
            }

            mdtNode.checkAnySameAllowedBeforeAfter(errorMessages, allTemplatedNodes)
        }
}

/**
 *  Собираем предыдущий Node пока не наткнёмся на шаблонный узел или null.
 */
private fun Node.accumulatePreviousNodeUntilFindTemplatedOrNull(
    allTemplatedNodes: Set<Node>,
    nonTemplatedNodes: MutableList<Node> = mutableListOf()
): List<Node> {
    if (this.previous != null && !allTemplatedNodes.contains(this.previous)) {
        nonTemplatedNodes.add(this.previous!!)
        this.previous?.accumulatePreviousNodeUntilFindTemplatedOrNull(allTemplatedNodes, nonTemplatedNodes)
    }
    return nonTemplatedNodes
}

/**
 *  Собираем следующий Node пока не наткнёмся на шаблонный узел или null.
 */
private fun Node.accumulateNextNodeUntilFindTemplatedOrNull(
    allTemplatedNodes: Set<Node>,
    nonTemplatedNodes: MutableList<Node> = mutableListOf()
): List<Node> {
    if (this.next != null && !allTemplatedNodes.contains(this.next)) {
        nonTemplatedNodes.add(this.next!!)
        this.next?.accumulatePreviousNodeUntilFindTemplatedOrNull(allTemplatedNodes, nonTemplatedNodes)
    }
    return nonTemplatedNodes
}

private fun getAllNodes(mdtNode: MDBaseNode, nodes: MutableSet<Node> = mutableSetOf()): Set<Node> {
    if (mdtNode.node != null) {
        nodes.add(mdtNode.node!!)
        mdtNode.children?.forEach { getAllNodes(it, nodes) }
    }
    return nodes
}

fun <E> Collection<E>.getPreviousBy(element: E): E? {
    return this.elementAtOrNull(this.indexOf(element) - 1)
}

fun <E> Collection<E>.getNextBy(element: E): E? {
    return this.elementAtOrNull(this.indexOf(element) + 1)
}


private fun Node.findSuitableNodeInPreviousOnesUntil(targetClasses: List<Class<*>>, node: Node?): List<Node> {
    val result = mutableListOf<Node>()
    var prevNode: Node? = this.previous
    while (prevNode != node && prevNode != null) {
        if (prevNode::class.java in targetClasses)
            result.add(prevNode)
        prevNode = prevNode.previous
    }
    return result
}

private fun Node.findSuitableNodeInNextOnesUntil(targetClasses: List<Class<*>>, node: Node?): List<Node> {
    val result = mutableListOf<Node>()
    var nextNode: Node? = this.next
    while (nextNode != node && nextNode != null) {
        if (nextNode::class.java in targetClasses)
            result.add(nextNode)
        nextNode = nextNode.next
    }
    return result
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
    var index = 0
    for (child in children) {
        if (child::class.java in templatableClasses) {
            val templateChild = createMDBaseNode(parent, child)
            templateChild.id = parent.id + "->child[$index]${templateChild::class.simpleName}"
            parent.children?.add(parseToTemplateRecursive(templateChild, child.children.toList()))
        } else {
            parseToTemplateRecursive(parent, child.children.toList())
        }
        index++
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

fun copyMDBaseNode(mdtNode: MDBaseNode): MDBaseNode {
    return when (mdtNode) {
        is MDTListNode -> MDTListNode(mdtNode)
        is MDTListItemNode -> MDTListItemNode(mdtNode)
        is MDTLinkNode -> MDTLinkNode(mdtNode)
        is MDTHeaderNode -> MDTHeaderNode(mdtNode)
        else -> throw RuntimeException("Node: $mdtNode is not copyable")
    }
}

val templatableClasses = MDTListNode.getTemplatebleClasses() + MDTListItemNode.getTemplatebleClasses() +
        MDTLinkNode.getTemplatebleClasses() + MDTHeaderNode.getTemplatebleClasses()

