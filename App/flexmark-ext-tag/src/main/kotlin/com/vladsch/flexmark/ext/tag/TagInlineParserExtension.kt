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

        if (isSurroundedBy(input, inlineParser.index, "[[", "]]"))
            return false

        if (inlineParser.index == input.length - 1) // чтобы не зацикливался на # в конце
            return false

        inlineParser.index =
            if (inlineParser.index == 0) 0 else inlineParser.index - 1 // Это нужно для того, чтобы inlineParser.matcher не брал false positive hashtag

        val matcher: Matcher? =
            inlineParser.matcher(Pattern.compile("([^а-яА-Яa-zA-Z0-9#\\[]|^)#{1}\\w+(/?\\w+)*([\\W&&[^#/]]|\$)"))
        /*
        TODO Здесь проблема в ссылке на сложность [[Описание хештегов#Сложность Описание хештегов Структурные роли узлов оценивающих узлов|сложность]]
        Если убрать хештег там, то нет ошибки парсинга Ошибка парсинга
        Такое чувство, что Tag парсер хватает, а WikiLink не успевает схватить и Tag парсер портит малину
         */
        if (matcher != null) {

            val startIndex = runCatching { matcher.end(1) }.getOrElse { matcher.start(0) }
            val endIndex = runCatching { matcher.start(3) }.getOrElse { matcher.end(0) }
            val hash: BasedSequence = input.subSequence(startIndex, startIndex + 1)
            val tag: BasedSequence = input.subSequence(startIndex + 1, endIndex)
            val hashTag = Tag(tag, hash)
            hashTag.setCharsFromContent()
            val textStartIndex = inlineParser.block.children.sumOf { it.textLength }
            if (startIndex != 0 && textStartIndex != 0 && textStartIndex <= startIndex) {
                inlineParser.appendText(inlineParser.input, textStartIndex, startIndex)
                inlineParser.flushTextNode()
            }
            inlineParser.block.appendChild(hashTag)
            return true
        } else {
            inlineParser.index++
        }
        return false
    }

    /**
     * Возвращает true, если символ по индексу @param indexCharWhichIsSurrounded встречается в @param input между @param byLeft и @param byRight
     */
    companion object {
        fun isSurroundedBy(
            input: BasedSequence,
            idxWhichIsSurrounded: Int,
            byLeft: String,
            byRight: String
        ): Boolean {
            val NOT_FOUND = -1
            val leftIdxOfByLeft = input.lastIndexOf(byLeft, 0, idxWhichIsSurrounded)
            val leftIdxOfByRight = input.lastIndexOf(byRight, 0, idxWhichIsSurrounded)
            if (leftIdxOfByLeft == NOT_FOUND || leftIdxOfByLeft <= leftIdxOfByRight && leftIdxOfByRight != NOT_FOUND) return false

            val rightIdxOfByLeft = input.indexOf(byLeft, idxWhichIsSurrounded, ignoreCase = true)
            val rightIdxOfByRight = input.indexOf(byRight, idxWhichIsSurrounded, ignoreCase = true)
            if (rightIdxOfByRight == NOT_FOUND || rightIdxOfByRight >= rightIdxOfByLeft && rightIdxOfByLeft != NOT_FOUND) return false

            return true
        }
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