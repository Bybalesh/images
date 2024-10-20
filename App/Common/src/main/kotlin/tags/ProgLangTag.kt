package tags


enum class ProgLangTag(val tag: String) : IMDTag<ProgLangTag> {
    PROG_LANG_JAVA("#PROG_LANG/JAVA"),
    PROG_LANG_GO("#PROG_LANG/GO"),
    PROG_LANG_ALL("#PROG_LANG/ALL");

    override fun tagsName(): String {
        return "Языки программирования"
    }

    override fun isOptional(): Boolean {
        return true
    }

    override fun tags(): List<ProgLangTag> {
        return entries
    }

    override fun tagOf(tag: String): ProgLangTag? {
        return tags().find { it.tag == tag }
    }

//    override fun isTag(tag: String): Boolean {
//        return tags().any(tag::equals)
//    }

    override fun tagsAsStr(): List<String> {
        return tags().map { it.tag }
    }

    companion object {
        fun tagOf(tag: String): ProgLangTag? {
            return entries.first().tagOf(tag)
        }
    }
}