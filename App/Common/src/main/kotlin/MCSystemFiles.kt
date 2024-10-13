import java.nio.file.Files
import java.nio.file.Paths

const val PATH_TO_TAGS_DESCRIPTION_MD = "../../docs/system/Описание хештегов.md"

object MCSystemFiles {

    fun getTagsDescription(): String {
        return Files.readString(Paths.get(PATH_TO_TAGS_DESCRIPTION_MD))
    }
}