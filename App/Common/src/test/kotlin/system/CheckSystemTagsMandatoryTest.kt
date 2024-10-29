package system

import CMSystemFiles.getSystemsMDNodes
import common.TestUtil
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import tags.StructRoleNodeTag

@DisplayName("Проверки для MD файлов в папке system")
class CheckSystemTagsMandatoryTest {

    @Test
    @DisplayName("Все md узлы в папке system, должны начинаться с Yaml Front Matter и обязан содержать tags #STRUCT/SYSTEM")
    fun checkSystemRuleWhatYamlFrontMatterExistsTest() {
        TestUtil.checkOnlyOneTagYamlFrontMatterContainsTest(
            getSystemsMDNodes(),
            StructRoleNodeTag.STRUCT_SYSTEM,
            false
        )
    }

}