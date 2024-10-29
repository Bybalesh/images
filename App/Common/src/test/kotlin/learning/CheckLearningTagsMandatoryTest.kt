package learning

import CMSystemFiles.getLearningMDNodes
import common.TestUtil
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import tags.StructRoleNodeTag

@DisplayName("Проверки для MD файлов в папке learning")
class CheckLearningTagsMandatoryTest {

    @Test
    @DisplayName("Все md узлы в папке learning, должны начинаться с Yaml Front Matter и обязан содержать tags #STRUCT/LEARNING")
    fun checkLearningRuleWhatYamlFrontMatterExistsTest() {
        TestUtil.checkOnlyOneTagYamlFrontMatterContainsTest(
            getLearningMDNodes(),
            StructRoleNodeTag.STRUCT_LEARNING,
            true
        )
    }

}