package common

import ParseUtil
import getAllFrontMatterNodesFromFirstFMBlock
import org.junit.jupiter.api.Assertions.assertTrue
import tags.IMDTag
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.stream.Stream

class TestUtil {
    companion object {
        /**
         * Все md узлы в определённой папке , должны начинаться с Yaml Front Matter и обязаны содержать tags
         */
        fun <T : Enum<T>> checkOnlyOneTagYamlFrontMatterContainsTest(
            paths: Stream<Path>,
            tag: IMDTag<T>,
            isCanBeEmpty: Boolean,
            isCanContainsMoreWhenOneTag: Boolean,
        ) {
            var isMDNodesExists = false
            var errorMessages = mutableListOf<String>()

            paths
                .forEach { path ->
                    val foundedTags = ParseUtil.mdParser.parse(Files.readString(path))
                        .getAllFrontMatterNodesFromFirstFMBlock("tags")
                        .map("#"::plus)
                        .map { tag.tagOf(it) }
                        .filter(Objects::nonNull)

                    isMDNodesExists = true
                    if (foundedTags.size > 1 && !isCanContainsMoreWhenOneTag) {
                        errorMessages.add(
                            "$path содержит ${foundedTags.size} шт ${
                                tag.tags().first()::class
                            }:[$foundedTags]. Должен быть 1: $tag!"
                        )
                    }
                    if (!foundedTags.any { it == tag }) {
                        errorMessages.add(
                            "$path должен содержать: $tag в Yaml Front Matter!"
                        )
                    }
                }

            assertTrue(errorMessages.isEmpty(), errorMessages.joinToString("\n"))
            assertTrue(isMDNodesExists || isCanBeEmpty, "Path должен содержать MD файлы")
        }
    }
}