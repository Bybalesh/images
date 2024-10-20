package tags

enum class LanguageTag(val tag: String) : IMDTag<LanguageTag> {
    LANG_RU("#LANG/RU"),
    LANG_ENG("#LANG/ENG"),
    LANG_HIN("#LANG/HIN");

    override fun tagsName(): String {
        return "Языковые"
    }

    override fun isOptional(): Boolean {
        return false
    }

    override fun tags(): List<LanguageTag> {
        return entries
    }

    override fun tagOf(tag: String): LanguageTag? {
        return tags().find { it.tag == tag }
    }

//    override fun isTag(tag: String): Boolean {
//        return tags().any(tag::equals)
//    }

    override fun tagsAsStr(): List<String> {
        return tags().map { it.tag }
    }

    companion object {
        fun tagOf(tag: String): LanguageTag? {
            return entries.first().tagOf(tag)
        }
    }
}