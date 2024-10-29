package com.vladsch.flexmark.ext.tag

import com.vladsch.flexmark.parser.InlineParser
import com.vladsch.flexmark.parser.InlineParserExtension
import com.vladsch.flexmark.parser.InlineParserExtensionFactory
import com.vladsch.flexmark.parser.LightInlineParser
import com.vladsch.flexmark.util.sequence.BasedSequence
import java.util.regex.Matcher
import java.util.regex.Pattern


class TagInlineParserExtension(lightInlineParser: LightInlineParser) : InlineParserExtension {

    override fun finalizeDocument(inlineParser: InlineParser) {
    }

    override fun finalizeBlock(inlineParser: InlineParser) {
    }

    //Paragraph
    override fun parse(inlineParser: LightInlineParser): Boolean {
        val input = inlineParser.input
        if (inlineParser.index == input.length - 1) // чтобы не зацикливался на # в конце
            return false

        inlineParser.index =
            if (inlineParser.index == 0) 0 else inlineParser.index - 1 // Это нужно для того, чтобы inlineParser.matcher не брал false positive hashtag

        val matcher: Matcher? =
            inlineParser.matcher(Pattern.compile("([^а-яА-Яa-zA-Z0-9#\\[]|^)#{1}\\w+(/?\\w+)*([\\W&&[^#/]]|\$)"))

        if (matcher != null) {

            val startIndex = runCatching { matcher.end(1) }.getOrElse { matcher.start(0) }
            val endIndex = runCatching { matcher.start(3) }.getOrElse { matcher.end(0) }
            val hash: BasedSequence = input.subSequence(startIndex, startIndex + 1)
            val tag: BasedSequence = input.subSequence(startIndex + 1, endIndex)
            val hashTag = Tag(tag, hash)
            hashTag.setCharsFromContent()
            val textStartIndex = inlineParser.block.children.sumOf { it.textLength }
            inlineParser.appendText(inlineParser.input, textStartIndex, startIndex)
            inlineParser.flushTextNode()
            inlineParser.block.appendChild(hashTag)
            return true
        } else {
            inlineParser.index++
        }
        return false
    }

    class Factory : InlineParserExtensionFactory {
        override fun getAfterDependents(): Set<Class<*>>? {
            return null
        }

        override fun getCharacters(): CharSequence {
            return "#"
        }

        override fun getBeforeDependents(): Set<Class<*>>? {
            return null
        }

        override fun apply(lightInlineParser: LightInlineParser): InlineParserExtension {
            return TagInlineParserExtension(lightInlineParser)
        }

        override fun affectsGlobalScope(): Boolean {
            return false
        }
    }


}