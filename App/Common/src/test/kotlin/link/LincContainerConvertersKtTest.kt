package link

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.file.Path

class LincContainerConvertersKtTest {

    @Test
    fun relativeByCurrent1Test() {

        val pathToFile = Path.of("../../docs/topic/Spring Web on Reactive Stack.md")

        val pathFromLink = Path.of("../system/Шаблон оценочного узла c тестом.md")

        val res = pathToFile.relativeByCurrent(pathFromLink).toString()

        assertEquals("../../docs/system/Шаблон оценочного узла c тестом.md", res)
    }

    @Test
    fun relativeByCurrent2Test() {

        val pathToFile = Path.of("../../docs/topic/Spring Web on Reactive Stack.md")

        val pathFromLink = Path.of("Ant.md")

        val res = pathToFile.relativeByCurrent(pathFromLink).toString()

        assertEquals("../../docs/topic/Ant.md", res)
    }
}