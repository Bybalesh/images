package link

import com.vladsch.flexmark.util.ast.Node
import java.net.URI
import java.nio.file.Path

/**
 * Класс содержит в себе достаточную информацию, чтобы определить корректность ссылки
 * @param label - текст, что отображается в ссылке
 * @param pathToCurrentFile - путь до файла с ссылкой
 * @param pathToFile - путь до файла на который указывает ссылка
 * @param mDElementRegex - путь до элемента MD файла на который указывает ссылка
 */
data class RelatedLinkContainer<T : Node>(
    val label: String,
    val uri: URI,
    val isFile: Boolean,
    val pathToFile: Path,
    val mDElementRegex: List<Regex>?,
    val targetLink: T,
)
