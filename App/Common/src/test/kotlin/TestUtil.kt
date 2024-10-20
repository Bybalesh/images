import org.junit.platform.commons.util.ClassFilter
import org.junit.platform.commons.util.ReflectionUtils
import tags.EstimatingComplexityTag
import tags.IMDTag

object TestUtil {
    fun getTagsEnums() = ReflectionUtils.findAllClassesInPackage(
        EstimatingComplexityTag::class.java.packageName,
        ClassFilter.of {
            it.interfaces.any { it.name == IMDTag::class.qualifiedName }
        }).map { it.enumConstants.first() } as List<IMDTag<*>>
}