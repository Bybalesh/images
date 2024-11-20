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
    var charsRegex: String? = null,// Если = null, то не проверяется по принадлежности к templatableClasses
    var children: MutableList<MDBaseNode>? = null,
    var strictChildrenOrder: Boolean? = null,
    var anySameNotTemplatedNode_BeforeAllowed: Boolean? = null,
    var anySameNotTemplatedNode_AfterAllowed: Boolean? = null,
    open var templatableClasses: Set<Class<*>>? = null,
    open var specificNextNode: Set<Node>? = null,//TODO пока не используется в схемах
    @JsonIgnore
    var parent: MDBaseNode? = null,
    var nodeText: String? = null,
    var multiNodes: Boolean? = null,// Это означает, что узел шаблона может дублироваться, если он соответствует нескольким MD узлам и не optional
    var mustHaveChildren: Boolean? = null,// Обязан ли иметь детей узел? TODO реализовать
) {
    constructor(mdtNode: MDBaseNode) : this(//copy constructor
        id = mdtNode.id,//TODO переписать в template path. Есть проблема когда копируется родитель, то не изменяется путь у ребёнка
        node = mdtNode.node,
        optional = mdtNode.optional,
        charsRegex = mdtNode.charsRegex,
        children = mdtNode.children?.map(::copyMDBaseNode)?.toMutableList(),
        strictChildrenOrder = mdtNode.strictChildrenOrder,
        anySameNotTemplatedNode_BeforeAllowed = mdtNode.anySameNotTemplatedNode_BeforeAllowed,
        anySameNotTemplatedNode_AfterAllowed = mdtNode.anySameNotTemplatedNode_AfterAllowed,
        templatableClasses = mdtNode.templatableClasses,
        specificNextNode = mdtNode.specificNextNode,
        parent = mdtNode.parent,
        nodeText = null,
        multiNodes = mdtNode.multiNodes
    )
}

interface MDTemplateble {
    fun getTemplatebleClasses(): Set<Class<*>>
}