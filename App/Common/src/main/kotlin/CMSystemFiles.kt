import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.function.Predicate.not
import java.util.stream.Stream

const val PATH_TO_DOCS_ROOT = "../../docs"
const val PATH_TO_TAGS_DESCRIPTION_MD = "$PATH_TO_DOCS_ROOT/system/Описание хештегов.md"

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
            .filter(Files::isRegularFile)
            .filter(Files::isReadable)
            .filter(not(Files::isDirectory))
            .filter { it.fileName.toString().endsWith(".md") }
    }
}