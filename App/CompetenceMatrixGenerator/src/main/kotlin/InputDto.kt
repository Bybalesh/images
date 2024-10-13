package ru.mc.generator

import tags.LanguageTag
import tags.LanguageTag.LANG_RU
import tags.ProgLangTag
import tags.RoleTag

data class InputDto(
    val language: LanguageTag = LANG_RU,
    val roles: List<RoleTag> = listOf(RoleTag.ROLE_ALL),
    val programmingLanguages: List<ProgLangTag> = listOf(ProgLangTag.PROG_LANG_ALL),
    val includesTopicsWithoutRoles: Boolean? = true,
    val additionalTopics: List<String>? = emptyList(),
    val excludeTopics: List<String>? = emptyList()
)
