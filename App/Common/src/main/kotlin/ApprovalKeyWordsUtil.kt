object ApprovalKeyWordsUtil {
    // в нижнем регистре
    val agreeAliases = listOf("yes", "да", "+", "y", "x", "х") // in lower case
    val disagreeAliases = listOf("no", "нет", "-", "n", "[ ]", "( )") // in lower case

    fun String.isContainsApprovingWithoutDigitsAndLettersAround(): Boolean {
        return checkString(this, agreeAliases) || checkString(this, disagreeAliases)
    }

    fun String.isAgree(): Boolean {
        return checkString(this, agreeAliases)
    }

    fun checkString(input: String, aliases: List<String>): Boolean {
        for (alias in aliases) {
            val pattern = if (alias.first().isLetter()
            ) """((\b)$alias(\b))""".toRegex(setOf(RegexOption.IGNORE_CASE))
            else """((\B)${Regex.escape(alias)}(\B))""".toRegex(setOf(RegexOption.IGNORE_CASE))

            if (pattern.containsMatchIn(input)) {
                return true
            }
        }
        return false
    }
}