import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterBlock
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterNode
import com.vladsch.flexmark.util.ast.Node
import com.vladsch.flexmark.util.sequence.BasedSequence
import java.util.function.Predicate
import kotlin.reflect.KClass

fun Node.getAllFrontMatterNodesFromFirstFMBlock(nodeName: String): List<BasedSequence> {
    return this.getFirstChildAny(YamlFrontMatterBlock::class.java)
        ?.children
        ?.find { it is YamlFrontMatterNode && it.key.equals(nodeName, true) }
        ?.children
        ?.map(Node::getChars)
        ?: emptyList()
}

fun <T : Node> Node.getChildrenOfType(clazz: KClass<T>, isIt: Predicate<T>? = null): List<T> {
    return this.getChildrenOfType(listOf(clazz), isIt) as List<T>
}

fun <T : Node> Node.getChildrenOfType(clazzes: List<KClass<*>>, isIt: Predicate<T>? = null): List<T> {
    val result = mutableListOf<T>()

    for (child in children) {
        if (clazzes.any { it.isInstance(child) } && isIt?.test(child as T) ?: true)
            result.add(child as T)
        result.addAll(child.getChildrenOfType(clazzes, isIt))
    }
    return result
}