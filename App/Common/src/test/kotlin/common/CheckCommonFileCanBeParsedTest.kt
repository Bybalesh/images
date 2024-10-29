package common

import CMSystemFiles.getAllMDNodes
import ParseUtil
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.nio.file.Files
import kotlin.test.assertTrue

@DisplayName("Общие структурные правила касающиеся ссылок для всех узлов")
class CheckCommonFileCanBeParsedTest {

    @Test
    @DisplayName("Все md файлы могут распарситься без ошибки")
    fun checkCommonRuleWhatFilesByLinksExistsTest() {
        val errMsg = mutableListOf<String>()
        getAllMDNodes()
            .forEach { path ->
                try {
                    ParseUtil.mdParser.parse(Files.readString(path))
                } catch (exc: Exception) {
                    errMsg.add("Парсер не справился с $path из-за $exc")
                }
            }
        assertTrue(errMsg.isEmpty(), errMsg.joinToString("\n"))
    }
}

