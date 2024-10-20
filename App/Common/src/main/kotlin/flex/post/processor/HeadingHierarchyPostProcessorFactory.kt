package flex.post.processor

import com.vladsch.flexmark.parser.PostProcessor
import com.vladsch.flexmark.parser.block.DocumentPostProcessorFactory
import com.vladsch.flexmark.util.ast.Document


class HeadingHierarchyPostProcessorFactory : DocumentPostProcessorFactory() {

    override fun apply(document: Document): PostProcessor {
        return HeadingHierarchyPostProcessor()
    }

}