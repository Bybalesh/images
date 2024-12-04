package tag

import ParseUtil
import com.vladsch.flexmark.ext.tag.Tag
import com.vladsch.flexmark.ext.wikilink.WikiLink
import getChildrenOfType
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertEquals

class TagExtTest {
    @Test
    @DisplayName("MD должен парситься без потери тегов и ссылок")
    fun checkCommonFileAndHeaderByWikiLinkExistsTest() {

        val path = Path.of("src/test/resources/tagAndLinks.md")

        assertEquals(
            2, ParseUtil.mdParser.parse(Files.readString(path))
                .getChildrenOfType(WikiLink::class)
                .count()
        )
        assertEquals(
            5, ParseUtil.mdParser.parse(Files.readString(path))
                .getChildrenOfType(Tag::class)
                .count()
        )


    }
}