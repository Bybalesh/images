import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
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

    val jsonMapper = jacksonObjectMapper().registerKotlinModule()
        //        ParseUtil.jsonMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true)
        .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
        .setSerializationInclusion(JsonInclude.Include.NON_ABSENT)
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
//        .setFilterProvider(
//            SimpleFilterProvider().addFilter(
//                "node", SimpleBeanPropertyFilter.filterOutAllExcept("1node")
//            )
//        )
        .setSerializationInclusion(JsonInclude.Include.NON_DEFAULT)

    private
    val flexmarkExtensions = mutableListOf(
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