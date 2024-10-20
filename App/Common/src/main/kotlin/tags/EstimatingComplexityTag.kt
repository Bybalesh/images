package tags

enum class EstimatingComplexityTag(val tag: String, weight: Int) : IMDTag<EstimatingComplexityTag> {
    COMPLEXITY_BASIC("#COMPLEXITY/BASIC", 5),
    COMPLEXITY_MEDIUM("#COMPLEXITY/MEDIUM", 10),
    COMPLEXITY_ADVANCED("#COMPLEXITY/ADVANCED", 20);

    override fun tagsName(): String {
        return "Сложность"
    }

    override fun isOptional(): Boolean {
        return false
    }

    override fun tags(): List<EstimatingComplexityTag> {
        return entries
    }

    override fun tagOf(tag: String): EstimatingComplexityTag? {
        return tags().find { it.tag == tag }
    }

//    override fun isTag(tag: String): Boolean {
//        return tags().any(tag::equals)
//    }

    override fun tagsAsStr(): List<String> {
        return tags().map { it.tag }
    }

    companion object {
        fun tagOf(tag: String): EstimatingComplexityTag? {
            return entries.first().tagOf(tag)
        }
    }
}