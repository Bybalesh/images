package com.vladsch.flexmark.ext.tag

import com.vladsch.flexmark.ext.tag.TagInlineParserExtension.Companion.isSurroundedBy
import com.vladsch.flexmark.util.sequence.BasedSequence
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TagInlineParserExtensionTest {
    @Test
    fun isSurroundedByTest() {

        assertTrue(isSurroundedBy(BasedSequence.of("[[#]]"), "[[#]]".indexOf("#"), "[[", "]]"))
        assertFalse(isSurroundedBy(BasedSequence.of("#]]"), "#]]".indexOf("#"), "[[", "]]"))
        assertFalse(isSurroundedBy(BasedSequence.of("[[#"), "[[#".indexOf("#"), "[[", "]]"))
        assertTrue(isSurroundedBy(BasedSequence.of("123[[#]]321"), "123[[#]]321".indexOf("#"), "[[", "]]"))
        assertTrue(isSurroundedBy(BasedSequence.of("123[[123#321|#123321]]321"), "123[[123#321|#123321]]321".indexOf("#"), "[[", "]]"))
        assertFalse(isSurroundedBy(BasedSequence.of("123[[1]]23#321|#123321]]321"), "123[[1]]23#321|#123321]]321".indexOf("#"), "[[", "]]"))
        assertFalse(isSurroundedBy(BasedSequence.of("123[[123#321|#12332[[1]]321"), "123[[123#321|#12332[[1]]321".indexOf("#"), "[[", "]]"))
        assertFalse(isSurroundedBy(BasedSequence.of("123[[EMPTY]]321"), "123[[EMPTY]]321".indexOf("#"), "[[", "]]"))
    }
}