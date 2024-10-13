package tags

const val DESCRIPTION_HEADER3 = "### Описание"
const val TAG_LIST_HEADER3 = "### Список:"

object TagUtil {
    fun expandAndClearTags(tags: List<String>): List<String> {
        val expandedTags = tags.map(String::trim).toMutableSet()
        val additionalTags: MutableList<String> = mutableListOf()

        expandedTags
            .reduce { acc, str ->
                if (str.startsWith("/")) {
                    additionalTags.add("$acc$str")
                    acc
                } else {
                    str
                }
            }

        expandedTags.filter { it.startsWith("#") }
            .forEach {
                it.split("/")
                    .reduce { acc, str -> if (acc.contains("/")) additionalTags.add(acc); "$acc/$str" }
            }
        expandedTags.addAll(additionalTags)

        return expandedTags.filter { it.startsWith("#") }
    }
}