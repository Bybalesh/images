import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class JustFailTest {

    @Test
    fun failTest() {
        assertFalse(true, "Тест упал просто так")
    }


}