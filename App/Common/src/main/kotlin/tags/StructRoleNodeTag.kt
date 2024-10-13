package tags

enum class StructRoleNodeTag(val tag: String) {
    STRUCT_SYSTEM("#STRUCT/SYSTEM"),
    STRUCT_ESTIMATING_("#STRUCT/ESTIMATING"),
    STRUCT_ESTIMATING_TASK("#STRUCT/ESTIMATING/TASK"),
    STRUCT_ESTIMATING_QUESTION("#STRUCT/ESTIMATING/QUESTION"),
    STRUCT_ESTIMATING_TEST("#STRUCT/ESTIMATING/TEST"),
    STRUCT_ESTIMATING_CODE("#STRUCT/ESTIMATING/CODE"),
    STRUCT_LEARNING("#STRUCT/LEARNING"),
    STRUCT_TOPIC("#STRUCT/TOPIC"),
    STRUCT_UNION("#STRUCT/UNION")
}