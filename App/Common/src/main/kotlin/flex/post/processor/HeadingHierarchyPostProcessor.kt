package flex.post.processor

import com.vladsch.flexmark.ast.Heading
import com.vladsch.flexmark.parser.block.DocumentPostProcessor
import com.vladsch.flexmark.util.ast.Document
import java.util.*

/**
 * Изначально Heading не иерархическая структура.
 * Данный процессор вкладывает в children Header другие headers более низкого уровня
 */
class HeadingHierarchyPostProcessor : DocumentPostProcessor() {
    override fun processDocument(document: Document): Document {

        val headingStack = ArrayDeque<Heading>()
        document.children.forEach {
            if (it is Heading) {
                while (headingStack.peek() != null && headingStack.peek().level >= it.level) headingStack.poll() // зачищаем стек

                headingStack.peek()?.appendChild(it)
                headingStack.push(it)


            } else {
                headingStack.peek()?.appendChild(it)
            }
        }

//        printAllChild(document as Node, java.lang.StringBuilder(), "") TODO для дебага
        return document
    }

//    fun printAllChild(node: Node, bb: StringBuilder, tabs: String): StringBuilder { TODO для дебага
//        bb.append(tabs)
//        bb.append(node.toString())
//        if (node is Heading)
//            bb.append((node as Heading).text)
//        bb.append('\n')
//        node.children.forEach { printAllChild(it, bb, tabs + "\t->") }
//        return bb
//    }


}