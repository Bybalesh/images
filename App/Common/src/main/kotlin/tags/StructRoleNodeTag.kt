package tags

enum class StructRoleNodeTag(val tag: String) : IMDTag<StructRoleNodeTag> {
    STRUCT_SYSTEM("#STRUCT/SYSTEM"),
    STRUCT_ESTIMATING_("#STRUCT/ESTIMATING"),
    STRUCT_ESTIMATING_TASK("#STRUCT/ESTIMATING/TASK"),
    STRUCT_ESTIMATING_QUESTION("#STRUCT/ESTIMATING/QUESTION"),
    STRUCT_ESTIMATING_TEST("#STRUCT/ESTIMATING/TEST"),
    STRUCT_ESTIMATING_CODE("#STRUCT/ESTIMATING/CODE"),
    STRUCT_LEARNING("#STRUCT/LEARNING"),
    STRUCT_TOPIC("#STRUCT/TOPIC"),
    STRUCT_GENERATED("#STRUCT/GENERATED"),
    STRUCT_UNION("#STRUCT/UNION");

    override fun tagsName(): String {
        return "Структурные роли узлов"
    }

    override fun isOptional(): Boolean {
        return false
    }

    override fun tags(): List<StructRoleNodeTag> {
        return entries
    }

    override fun tagOf(tag: String): StructRoleNodeTag? {
        return tags().find { it.tag == tag }
    }

//    override fun isTag(tag: String): Boolean {
//        return tags().any(tag::equals)
//    }

    override fun tagsAsStr(): List<String> {
        return tags().map { it.tag }
    }

    companion object {
        fun tagOf(tag: String): StructRoleNodeTag? {
            return entries.first().tagOf(tag)
        }
    }
}