package template

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.vladsch.flexmark.util.ast.Node
import kotlin.reflect.KClass

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.CLASS)
@JsonPropertyOrder(value = ["nodeType", "level", "optional", "charsRegex", "templatableClasses", "strictChildrenOrder", "specificNextNode", "anySameNodeBeforeChildrenAllowed", "anySameNodeBetweenChildrenAllowed", "anySameNodeAfterChildrenAllowed", "children"])
sealed class MDBaseNode(
    @JsonIgnore
    open var node: Node? = null,
    var optional: Boolean? = null,
    var charsRegex: String? = null,
    var children: MutableList<MDBaseNode>? = null,
    var strictChildrenOrder: Boolean? = null,
    var anySameNodeBeforeChildrenAllowed: Boolean? = null,
    var anySameNodeBetweenChildrenAllowed: Boolean? = null,
    var anySameNodeAfterChildrenAllowed: Boolean? = null,
    open var templatableClasses: Set<Class<*>>? = null,
    open var specificNextNode: Set<Node>? = null,
    @JsonIgnore
    var parent: MDBaseNode? = null,
)

interface MDTemplateble {
    fun getTemplatebleClasses(): Set<Class<*>>
}