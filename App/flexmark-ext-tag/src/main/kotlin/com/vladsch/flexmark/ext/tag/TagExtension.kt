package com.vladsch.flexmark.ext.tag

import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.data.MutableDataHolder

class TagExtension private constructor() : Parser.ParserExtension {

    override fun extend(parserBuilder: Parser.Builder) {
        parserBuilder.customInlineParserExtensionFactory(TagInlineParserExtension.Factory())
    }

    override fun parserOptions(options: MutableDataHolder?) {}//TODO parse custom only

    companion object {
        fun create(): TagExtension {
            return TagExtension()
        }
    }
}