package estimating

import CMSystemFiles.getEstimatingMDNodes
import common.TestUtil
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import tags.StructRoleNodeTag
import java.nio.file.Path

@DisplayName("Проверки для MD файлов в папке estimating")
class CheckEstimatingTagsMandatoryTest {

    @Test
    @DisplayName("Все md узлы в папке estimating, должны начинаться с Yaml Front Matter и обязан содержать tags #STRUCT/ESTIMATING")
    fun checkEstimatingRuleWhatYamlFrontMatterExistsTest() {

        println("REAL PATH TO TEST" + Path.of(".").toAbsolutePath())

        TestUtil.checkOnlyOneTagYamlFrontMatterContainsTest(
            getEstimatingMDNodes(),
            StructRoleNodeTag.STRUCT_ESTIMATING_,
            true
        )
    }

}