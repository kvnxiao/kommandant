import com.github.kvnxiao.kommandant.Kommandant
import com.github.kvnxiao.kommandant.command.CommandBuilder
import com.github.kvnxiao.kommandant.command.CommandBuilder.Companion.executor
import org.junit.BeforeClass
import org.junit.Test
import testcommands.KotlinBasedCommands

/**
 * Created on:   2017-03-05
 * Author:       Kevin Xiao (github.com/alphahelix00)
 *
 */
class KotlinTest {

    companion object {

        private val kommandant = Kommandant()

        @BeforeClass
        @JvmStatic
        fun setup() {
            kommandant.addAnnotatedCommands(KotlinBasedCommands::class)
            kommandant.addCommand(CommandBuilder<Unit>("test").withAliases("a").build(executor {
                context, opt ->
                println("HELLO WORLD!")
            }))
        }
    }

    @Test
    fun test() {
        kommandant.process<Any>("/kotlin")
        println(kommandant.process<Any>("/a"))
    }

}