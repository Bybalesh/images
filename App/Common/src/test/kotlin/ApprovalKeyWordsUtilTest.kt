import ApprovalKeyWordsUtil.isContainsApprovingWithoutDigitsAndLettersAround
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class ApprovalKeyWordsUtilTest {

    @Test
    fun testAgreeAliases() {
        " yes".isContainsApprovingWithoutDigitsAndLettersAround();

        val agreeAliasesWithNumberInStart = ApprovalKeyWordsUtil.agreeAliases.map { "1" + it }

        agreeAliasesWithNumberInStart.forEach {
            assertFalse(
                it.isContainsApprovingWithoutDigitsAndLettersAround(),
                "[$it] не проходит"
            )
        }

        val agreeAliasesWithNumberInEnd = ApprovalKeyWordsUtil.agreeAliases.map { it + "9" }

        agreeAliasesWithNumberInEnd.forEach {
            assertFalse(
                it.isContainsApprovingWithoutDigitsAndLettersAround(),
                "[$it] не проходит"
            )
        }

        val agreeAliasesWithNumberInStartAndEnd = ApprovalKeyWordsUtil.agreeAliases.map { "1" + it + "9" }

        agreeAliasesWithNumberInStartAndEnd.forEach {
            assertFalse(
                it.isContainsApprovingWithoutDigitsAndLettersAround(),
                "[$it] не проходит"
            )
        }

        val agreeAliasesWithLetterInStart = ApprovalKeyWordsUtil.agreeAliases.map { "a" + it }

        agreeAliasesWithLetterInStart.forEach {
            assertFalse(
                it.isContainsApprovingWithoutDigitsAndLettersAround(),
                "[$it] не проходит"
            )
        }

        val agreeAliasesWithLetterInEnd = ApprovalKeyWordsUtil.agreeAliases.map { it + "v" }

        agreeAliasesWithLetterInEnd.forEach {
            assertFalse(
                it.isContainsApprovingWithoutDigitsAndLettersAround(),
                "[$it] не проходит"
            )
        }

        val agreeAliasesWithLetterInStartAndEnd = ApprovalKeyWordsUtil.agreeAliases.map { "c" + it + "h" }

        agreeAliasesWithLetterInStartAndEnd.forEach {
            assertFalse(
                it.isContainsApprovingWithoutDigitsAndLettersAround(),
                "[$it] не проходит"
            )
        }

        val agreeAliasesWithSpaceInStart = ApprovalKeyWordsUtil.agreeAliases.map { " " + it }

        agreeAliasesWithSpaceInStart.forEach {
            assertTrue(
                it.isContainsApprovingWithoutDigitsAndLettersAround(),
                "[$it] не проходит"
            )
        }

        val agreeAliasesWithSpaceInEnd = ApprovalKeyWordsUtil.agreeAliases.map { it + " " }

        agreeAliasesWithSpaceInEnd.forEach {
            assertTrue(
                it.isContainsApprovingWithoutDigitsAndLettersAround(),
                "[$it] не проходит"
            )
        }
        val agreeAliasesWithSpaceInStartAndEnd = ApprovalKeyWordsUtil.agreeAliases.map { " " + it + " " }

        agreeAliasesWithSpaceInStartAndEnd.forEach {
            assertTrue(
                it.isContainsApprovingWithoutDigitsAndLettersAround(),
                "[$it] не проходит"
            )
        }
        val agreeAliasesWithSymbolInStart = ApprovalKeyWordsUtil.agreeAliases.map { "!" + it }

        agreeAliasesWithSymbolInStart.forEach {
            assertTrue(
                it.isContainsApprovingWithoutDigitsAndLettersAround(),
                "[$it] не проходит"
            )
        }
        val agreeAliasesWithSymbolInEnd = ApprovalKeyWordsUtil.agreeAliases.map { it + "!" }

        agreeAliasesWithSymbolInEnd.forEach {
            assertTrue(
                it.isContainsApprovingWithoutDigitsAndLettersAround(),
                "[$it] не проходит"
            )
        }

        val agreeAliasesWithSymbolInStartAndEnd = ApprovalKeyWordsUtil.agreeAliases.map { "!" + it + "!" }

        agreeAliasesWithSymbolInStartAndEnd.forEach {
            assertTrue(
                it.isContainsApprovingWithoutDigitsAndLettersAround(),
                "[$it] не проходит"
            )
        }


        val disagreeAliasesWithNumberInStart = ApprovalKeyWordsUtil.disagreeAliases.map { "1" + it }

        disagreeAliasesWithNumberInStart.forEach {
            assertFalse(
                it.isContainsApprovingWithoutDigitsAndLettersAround(),
                "[$it] не проходит"
            )
        }

        val disagreeAliasesWithNumberInEnd = ApprovalKeyWordsUtil.disagreeAliases.map { it + "9" }

        disagreeAliasesWithNumberInEnd.forEach {
            assertFalse(
                it.isContainsApprovingWithoutDigitsAndLettersAround(),
                "[$it] не проходит"
            )
        }

        val disagreeAliasesWithNumberInStartAndEnd = ApprovalKeyWordsUtil.disagreeAliases.map { "1" + it + "9" }

        disagreeAliasesWithNumberInStartAndEnd.forEach {
            assertFalse(
                it.isContainsApprovingWithoutDigitsAndLettersAround(),
                "[$it] не проходит"
            )
        }

        val disagreeAliasesWithLetterInStart = ApprovalKeyWordsUtil.disagreeAliases.map { "a" + it }

        disagreeAliasesWithLetterInStart.forEach {
            assertFalse(
                it.isContainsApprovingWithoutDigitsAndLettersAround(),
                "[$it] не проходит"
            )
        }

        val disagreeAliasesWithLetterInEnd = ApprovalKeyWordsUtil.disagreeAliases.map { it + "v" }

        disagreeAliasesWithLetterInEnd.forEach {
            assertFalse(
                it.isContainsApprovingWithoutDigitsAndLettersAround(),
                "[$it] не проходит"
            )
        }

        val disagreeAliasesWithLetterInStartAndEnd = ApprovalKeyWordsUtil.disagreeAliases.map { "c" + it + "h" }

        disagreeAliasesWithLetterInStartAndEnd.forEach {
            assertFalse(
                it.isContainsApprovingWithoutDigitsAndLettersAround(),
                "[$it] не проходит"
            )
        }

        val disagreeAliasesWithSpaceInStart = ApprovalKeyWordsUtil.disagreeAliases.map { " " + it }

        disagreeAliasesWithSpaceInStart.forEach {
            assertTrue(
                it.isContainsApprovingWithoutDigitsAndLettersAround(),
                "[$it] не проходит"
            )
        }

        val disagreeAliasesWithSpaceInEnd = ApprovalKeyWordsUtil.disagreeAliases.map { it + " " }

        disagreeAliasesWithSpaceInEnd.forEach {
            assertTrue(
                it.isContainsApprovingWithoutDigitsAndLettersAround(),
                "[$it] не проходит"
            )
        }
        val disagreeAliasesWithSpaceInStartAndEnd = ApprovalKeyWordsUtil.disagreeAliases.map { " " + it + " " }

        disagreeAliasesWithSpaceInStartAndEnd.forEach {
            assertTrue(
                it.isContainsApprovingWithoutDigitsAndLettersAround(),
                "[$it] не проходит"
            )
        }
        val disagreeAliasesWithSymbolInStart = ApprovalKeyWordsUtil.disagreeAliases.map { "!" + it }

        disagreeAliasesWithSymbolInStart.forEach {
            assertTrue(
                it.isContainsApprovingWithoutDigitsAndLettersAround(),
                "[$it] не проходит"
            )
        }
        val disagreeAliasesWithSymbolInEnd = ApprovalKeyWordsUtil.disagreeAliases.map { it + "!" }

        disagreeAliasesWithSymbolInEnd.forEach {
            assertTrue(
                it.isContainsApprovingWithoutDigitsAndLettersAround(),
                "[$it] не проходит"
            )
        }

        val disagreeAliasesWithSymbolInStartAndEnd = ApprovalKeyWordsUtil.disagreeAliases.map { "!" + it + "!" }

        disagreeAliasesWithSymbolInStartAndEnd.forEach {
            assertTrue(
                it.isContainsApprovingWithoutDigitsAndLettersAround(),
                "[$it] не проходит"
            )
        }


    }


}