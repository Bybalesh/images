package tags

interface IMDTag<T : Enum<T>> {

    /**
     * @return имя тега
     */
    fun tagsName(): String

//    /**
//     * @return true если тег с таким именем существует
//     */
//    fun isTag(tag: String): Boolean

    /**
     * @return enum если тег с таким именем существует
     */
    fun tagOf(tag: String): T?

    /**
     * @return все теги
     */
    fun tags(): List<T>

    /**
     * @return имя конкретного тега
     */
    fun tagsAsStr(): List<String>

    /**
     * @return имя конкретного тега
     */
    fun isOptional(): Boolean
}