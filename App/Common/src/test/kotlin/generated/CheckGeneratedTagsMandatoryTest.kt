package generated

import CMSystemFiles.getGeneratedMDNodes
import common.TestUtil
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import tags.StructRoleNodeTag

@DisplayName("Проверки для MD файлов в папке generated")
class CheckGeneratedTagsMandatoryTest {

    @Test
    @DisplayName("Все md узлы в папке generated, должны начинаться с Yaml Front Matter и обязан содержать tags #STRUCT/GENERATED")
    fun checkGeneratedRuleWhatYamlFrontMatterExistsTest() {
        TestUtil.checkOnlyOneTagYamlFrontMatterContainsTest(
            getGeneratedMDNodes(),
            StructRoleNodeTag.STRUCT_GENERATED,
            false
        )
    }

}