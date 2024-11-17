package template

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.vladsch.flexmark.util.ast.Node

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.CLASS)
@JsonPropertyOrder(value = ["id", "nodeType", "level", "optional", "charsRegex", "nodeText", "templatableClasses", "strictChildrenOrder", "specificNextNode", "anySameNotTemplatedNode_BeforeAllowed", "anySameNotTemplatedNode_AfterAllowed", "children"])
sealed class MDBaseNode(
    var id: String? = null,
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
    var nodeText: String? = null,
)

interface MDTemplateble {
    fun getTemplatebleClasses(): Set<Class<*>>
}