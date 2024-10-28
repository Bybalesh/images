package com.vladsch.flexmark.ext.tag

import com.vladsch.flexmark.util.ast.Block
import com.vladsch.flexmark.util.sequence.BasedSequence


class Tag(
    var tag: BasedSequence,
    var openingMarker: BasedSequence,
) : Block(openingMarker.baseSubSequence(openingMarker.startOffset, tag.endOffset)) {

    override fun getSegments(): Array<BasedSequence> {
        return arrayOf(openingMarker, tag)
    }

    override fun getAstExtra(out: StringBuilder) {
        segmentSpanChars(out, openingMarker, "open")
        segmentSpanChars(out, tag, "tag")
    }
}