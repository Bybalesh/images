package tags

enum class CompetenceLVLTag(val tag: String, rank: Int) : IMDTag<CompetenceLVLTag> {
    LVL_INTERN("#LVL/INTERN", 1),
    LVL_JUNIOR("#LVL/JUNIOR", 2),
    LVL_MIDDLE("#LVL/MIDDLE", 3),
    LVL_SENIOR("#LVL/SENIOR", 4);

    override fun tagsName(): String {
        return "Уровень компетенций"
    }

    override fun isOptional(): Boolean {
        return true
    }

    override fun tags(): List<CompetenceLVLTag> {
        return entries
    }

    override fun tagOf(tag: String): CompetenceLVLTag? {
        return tags().find { it.tag == tag }
    }

//    override fun isTag(tag: String): Boolean {
//        return tags().any(tag::equals)
//    }

    override fun tagsAsStr(): List<String> {
        return tags().map { it.tag }
    }

    companion object {
        fun tagOf(tag: String): CompetenceLVLTag? {
            return entries.first().tagOf(tag)
        }
    }
}