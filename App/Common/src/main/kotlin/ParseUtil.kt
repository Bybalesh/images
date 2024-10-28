import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.vladsch.flexmark.ext.footnotes.FootnoteExtension
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension
import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.ext.tag.TagExtension
import com.vladsch.flexmark.ext.wikilink.WikiLinkExtension
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension
import com.vladsch.flexmark.parser.Parser
import flex.post.processor.HeadingHierarchyPostProcessorFactory

object ParseUtil {

    val jsonMapper = jacksonObjectMapper()

    private val flexmarkExtensions = mutableListOf(
        TablesExtension.create(),
        StrikethroughExtension.create(),
        YamlFrontMatterExtension.create(),
        WikiLinkExtension.create(),
        FootnoteExtension.create(),
        TaskListExtension.create(),
        TagExtension.create()
    )

    val mdParser: Parser = Parser.builder()
        .extensions(flexmarkExtensions)
        .postProcessorFactory(HeadingHierarchyPostProcessorFactory())
        .build()

}