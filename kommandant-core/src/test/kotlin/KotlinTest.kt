import com.github.kvnxiao.kommandant.Kommandant
import com.github.kvnxiao.kommandant.command.CommandBuilder
import com.github.kvnxiao.kommandant.command.CommandBuilder.Companion.execute
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import testcommands.KotlinCommand

/**
 * Created on:   2017-03-05
 * Author:       Kevin Xiao (github.com/alphahelix00)
 *
 */
class KotlinTest {

    companion object {
        private val kommandant: Kommandant = Kommandant()
    }

    @After
    fun after() {
        kommandant.clearAll()
    }

    @Test
    fun testBasic() {
        kommandant.addAnnotatedCommands(KotlinCommand::class)
        kommandant.addCommand(CommandBuilder<Unit>("kotlintest").withPrefix("-").withAliases("ktest").build(execute {
            context, opt ->
            println("HELLO WORLD!")
        }))

        // Test command added with kotlin lambda
        val fail = kommandant.process<Unit>("/ktest")
        assertFalse(fail.success)
        val success = kommandant.process<Unit>("-ktest")
        assertTrue(success.success)

        // Test annotated command
        val annotated = kommandant.process<String>("/kotlin")
        assertTrue(annotated.success)
        assertEquals("this is a kotlin based command!", annotated.result)
    }

    @Test
    fun testDeleteCommand() {
        kommandant.addAnnotatedCommands(KotlinCommand::class)
        assertEquals(1, kommandant.getPrefixes().size)
        assertTrue(kommandant.getPrefixes().contains("/"))
        assertEquals(1, kommandant.getCommandsUnique().size)
        assertTrue(kommandant.process<String>("/kotlin").success)

        kommandant.deleteCommand(kommandant.getCommand("/kotlin"))
        assertFalse(kommandant.process<String>("/kotlin").success)
        assertTrue(kommandant.getPrefixes().isEmpty())
        assertTrue(kommandant.getCommandsUnique().isEmpty())
        assertTrue(kommandant.getAllCommands().isEmpty())
    }

}