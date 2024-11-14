package template

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.vladsch.flexmark.util.ast.Node

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.CLASS)
@JsonPropertyOrder(value = ["nodeType", "level", "optional", "charsRegex", "templatableClasses", "strictChildrenOrder", "specificNextNode", "anySameNodeBeforeChildrenAllowed", "anySameNodeBetweenChildrenAllowed", "anySameNotTemplatedNode_AfterAllowed", "children"])
sealed class MDBaseNode(
    @JsonIgnore
    open var node: Node? = null,
    var optional: Boolean? = null,
    var charsRegex: String? = null,
    var children: MutableList<MDBaseNode>? = null,
    var strictChildrenOrder: Boolean? = null,
    var anySameNotTemplatedNode_BeforeAllowed: Boolean? = null,
    var anySameNotTemplatedNode_AfterAllowed: Boolean? = null,
    open var templatableClasses: Set<Class<*>>? = null,
    open var specificNextNode: Set<Node>? = null,
    @JsonIgnore
    var parent: MDBaseNode? = null,
)

interface MDTemplateble {
    fun getTemplatebleClasses(): Set<Class<*>>
}