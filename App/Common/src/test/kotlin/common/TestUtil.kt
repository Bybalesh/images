package common

import ParseUtil
import getAllFrontMatterNodesFromFirstFMBlock
import org.junit.jupiter.api.Assertions.assertTrue
import tags.IMDTag
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Stream
import kotlin.test.assertEquals

class TestUtil {
    companion object {
        /**
         * Все md узлы в определённой папке , должны начинаться с Yaml Front Matter и обязаны содержать tags
         */
        fun <T : Enum<T>> checkOnlyOneTagYamlFrontMatterContainsTest(
            paths: Stream<Path>,
            tag: IMDTag<T>,
            isCanBeEmpty: Boolean,
        ) {
            var isMDNodesExists = false
            paths
                .forEach { path ->
                    println("READ FILE FROM: " + path)
                    val tagsCnt = ParseUtil.mdParser.parse(Files.readString(path))
                        .getAllFrontMatterNodesFromFirstFMBlock("tags")
                        .map("#"::plus)
                        .filter { tag.tagOf(it) == tag }
                    isMDNodesExists = true
                    assertEquals(
                        1,
                        tagsCnt.size,
                        "$path содержит ${tagsCnt.size} шт ${tag.tags().first()::class}:[$tagsCnt]. Должен 1! "
                    )
                }

            assertTrue(isMDNodesExists || isCanBeEmpty, "Path должен содержать MD файлы")
        }
    }
}