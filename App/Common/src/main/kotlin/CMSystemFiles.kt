import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.function.Predicate.not
import java.util.stream.Stream

const val PATH_TO_DOCS_ROOT = "../../docs"
const val PATH_TO_TAGS_DESCRIPTION_MD = "$PATH_TO_DOCS_ROOT/system/Описание хештегов.md"
const val PATH_TO_TOPICS_MD = "$PATH_TO_DOCS_ROOT/topic"
const val PATH_TO_SYSTEM_MD = "$PATH_TO_DOCS_ROOT/system"
const val PATH_TO_LEARNING_MD = "$PATH_TO_DOCS_ROOT/learning"
const val PATH_TO_GENERATED_MD = "$PATH_TO_DOCS_ROOT/generated"
const val PATH_TO_ESTIMATING_MD = "$PATH_TO_DOCS_ROOT/estimating"

object CMSystemFiles {

    /**
     * @return содержимое файла Описание хештегов.md
     */
    fun getTagsDescription(): String {
        return Files.readString(Paths.get(PATH_TO_TAGS_DESCRIPTION_MD))
    }

    /**
     * @return все md узлы/файлы
     */
    fun getAllMDNodes(): Stream<Path> {
        return Files.walk(Paths.get(PATH_TO_DOCS_ROOT))
            .filterMD()
    }

    /**
     * @return все md узлы/файлы из topic
     */
    fun getTopicsMDNodes(): Stream<Path> {
        return Files.walk(Paths.get(PATH_TO_TOPICS_MD))
            .filterMD()
    }

    /**
     * @return все md узлы/файлы из topic
     */
    fun getSystemsMDNodes(): Stream<Path> {
        return Files.walk(Paths.get(PATH_TO_SYSTEM_MD))
            .filterMD()
    }

    /**
     * @return все md узлы/файлы из topic
     */
    fun getLearningMDNodes(): Stream<Path> {
        return Files.walk(Paths.get(PATH_TO_LEARNING_MD))
            .filterMD()
    }

    /**
     * @return все md узлы/файлы из topic
     */
    fun getGeneratedMDNodes(): Stream<Path> {
        return Files.walk(Paths.get(PATH_TO_GENERATED_MD))
            .filterMD()
    }

    /**
     * @return все md узлы/файлы из topic
     */
    fun getEstimatingMDNodes(): Stream<Path> {
        return Files.walk(Paths.get(PATH_TO_ESTIMATING_MD))
            .filterMD()
    }

    private fun Stream<Path>.filterMD(): Stream<Path> {
        return this.filter(Files::isRegularFile)
            .filter(Files::isReadable)
            .filter(not(Files::isDirectory))
            .filter { it.fileName.toString().endsWith(".md") }
    }

}