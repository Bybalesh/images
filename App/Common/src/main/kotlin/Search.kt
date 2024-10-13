import org.intellij.markdown.IElementType
import org.intellij.markdown.MarkdownElementTypes.ATX_1
import org.intellij.markdown.MarkdownElementTypes.ATX_2
import org.intellij.markdown.MarkdownElementTypes.ATX_3
import org.intellij.markdown.MarkdownElementTypes.ATX_4
import org.intellij.markdown.MarkdownElementTypes.ATX_5
import org.intellij.markdown.MarkdownElementTypes.ATX_6
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode

fun ASTNode.findChildrenByTypeAndMaybeName(
    type: IElementType,
    rawMD: CharSequence,
    name: String = ""
): List<ASTNode> {
    return this.findChildrenByTypeAndMaybeName(listOf(type), rawMD, name)
}

fun ASTNode.findChildrenByTypeAndMaybeName(
    types: List<IElementType>,
    rawMD: CharSequence,
    name: String = ""
): List<ASTNode> {
    val children = this.children.toMutableSet()
    when (this.type) {
        ATX_1, ATX_2, ATX_3, ATX_4, ATX_5, ATX_6
            -> children.addAll(this.getParentsAllNodesBetweenThisAndNextSameHeaderLvlOrHigherLvl(rawMD))
    }

    return findChildRecursively(children, name, rawMD, types)
}

private fun ASTNode.findChildRecursively(
    children: Set<ASTNode>,
    name: String,
    rawMD: CharSequence,
    types: List<IElementType>
): List<ASTNode> {
    val result = mutableListOf<ASTNode>()
    for (child in children) {

        val sameNameOrNoMatter = name.isBlank() || name == child.getTextInNode(rawMD)

        if (types.contains(child.type) && sameNameOrNoMatter)
            result.add(child)

        result.addAll(child.findChildRecursively(child.children.toSet(), name, rawMD, types))

    }
    return result
}

/**
 * @return список всех узлов, которые находятся между текущим узлом заголовочного типа (УЗТ) и следующим УЗТ такого же или большего уровня
 */
fun ASTNode.getParentsAllNodesBetweenThisAndNextSameHeaderLvlOrHigherLvl(rawMD: CharSequence): List<ASTNode> {
    val nextSameChildStartOffset =
        this.getParentsFirstChildOfTheSameHeaderLvlOrHigherLvlAfterCurrentHeader()?.startOffset ?: Int.MAX_VALUE
    return this.parent?.children?.filter {
        it.startOffset > this.endOffset && it.startOffset < nextSameChildStartOffset
    }.orEmpty()
}

val orderedHeaders = listOf(ATX_6, ATX_5, ATX_4, ATX_3, ATX_2, ATX_1)

fun ASTNode.getParentsFirstChildOfTheSameHeaderLvlOrHigherLvlAfterCurrentHeader() =
//TODO если не хеадер, то бросать исключение
    this.parent?.findChildrenOfType(
        orderedHeaders.subList(orderedHeaders.indexOf(this.type), orderedHeaders.size),
        this
    )
        ?.firstOrNull()

private fun ASTNode.findChildrenOfType(types: List<IElementType>, afterNode: ASTNode? = null): List<ASTNode> {
    return children.filter {
        types.contains(it.type) && it.startOffset > (afterNode?.startOffset ?: 0)
    }
}
