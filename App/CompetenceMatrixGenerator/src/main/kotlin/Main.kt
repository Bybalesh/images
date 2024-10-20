package ru.mc.generator

//import com.intellij.openapi.project.ProjectManager
import ParseUtil

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
// {\"language\":\"LANG_RU\",\"roles\":[\"ROLE_DEV_BACKEND\"],\"programmingLanguages\":[\"PROG_LANG_JAVA\"],\"includesTopicsWithoutRoles\":true,\"additionalTopics\":[\"Build Tools\",\"Istio\"],\"excludeTopics\":[\"Java Garbage Collection\"]}
fun main(args: Array<String>) {
    val json = args.joinToString(separator = " ")


    val inputParams = ParseUtil.jsonMapper.readValue(json, InputDto::class.java)

}