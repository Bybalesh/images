package ru.mc.generator

//import com.intellij.openapi.project.ProjectManager
import OMUtil
import findChildrenByTypeAndMaybeName
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownElementTypes.ATX_1
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser
import java.nio.file.Files
import java.nio.file.Paths

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
// {\"language\":\"LANG_RU\",\"roles\":[\"ROLE_DEV_BACKEND\"],\"programmingLanguages\":[\"PROG_LANG_JAVA\"],\"includesTopicsWithoutRoles\":true,\"additionalTopics\":[\"Build Tools\",\"Istio\"],\"excludeTopics\":[\"Java Garbage Collection\"]}
fun main(args: Array<String>) {
    val json = args.joinToString(separator = " ")


    val inputParams = OMUtil.mapper.readValue(json, InputDto::class.java)


    val rawMDMC: String = Files.readString(Paths.get("../docs/system/Шаблон матрицы компетенций.md"))

    val flavour = GFMFlavourDescriptor()

    val parsedTree = MarkdownParser(flavour).parse(MarkdownElementTypes.MARKDOWN_FILE, rawMDMC, false)


    var aaa = parsedTree.findChildrenByTypeAndMaybeName(ATX_1, "# Матрица компетенций", rawMDMC)
//     ?.getTextInNode(rawMDMC).toString()


    val html = HtmlGenerator(rawMDMC, parsedTree, flavour).generateHtml()
    println(aaa)
}