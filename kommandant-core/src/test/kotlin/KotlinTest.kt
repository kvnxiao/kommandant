import com.github.kvnxiao.kommandant.Kommandant
import com.github.kvnxiao.kommandant.command.*
import com.github.kvnxiao.kommandant.command.CommandBuilder.Companion.execute
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import testcommands.KotlinCommand
import testcommands.VarargCommand

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
            opt.forEach(::println)
        }))

        // Test command added with kotlin lambda
        val fail = kommandant.process<Unit>("/ktest")
        assertFalse(fail.success)
        val success = kommandant.process<Unit>("-ktest", "hello", "world")
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

    @Test
    fun testVarargs() {
        kommandant.addCommand(CommandBuilder<Unit>("varargbuilder").withAliases("builder").build(execute {context, opt ->
           opt.forEach(::println)
        }))
        kommandant.addCommand(object : ICommand<Unit>(CommandDefaults.PREFIX, "varargconstructor", CommandDefaults.NO_DESCRIPTION, CommandDefaults.NO_USAGE, CommandDefaults.EXEC_WITH_SUBCOMMANDS, CommandDefaults.IS_DISABLED, "constructor") {
            override fun execute(context: CommandContext, vararg opt: Any?): Unit {
                opt.forEach(::println)
            }
        })
        kommandant.addAnnotatedCommands(VarargCommand::class)
        kommandant.process<Unit>("/builder", "this", "is", "a", "builder")
        kommandant.process<Unit>("/constructor", "this", "is", "a", "constructor")
        kommandant.process<Unit>("/annotation", "this", "is", "an", "annotation")
    }

}