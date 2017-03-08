import com.github.kvnxiao.kommandant.Kommandant
import com.github.kvnxiao.kommandant.command.CommandContext
import com.github.kvnxiao.kommandant.command.CommandDefaults
import com.github.kvnxiao.kommandant.command.ICommand
import com.github.kvnxiao.kommandant.configurable.CommandBankConfigurable
import org.junit.BeforeClass
import org.junit.Test

/**
 * Created by kxiao on 3/7/17.
 */
class TestConfigurable {
    companion object {

        private val kommandant = Kommandant(cmdBank = CommandBankConfigurable())

        @BeforeClass
        @JvmStatic
        fun setup() {
            kommandant.addCommand(object : ICommand<String>("/", "main", CommandDefaults.NO_DESCRIPTION, CommandDefaults.NO_USAGE, CommandDefaults.EXEC_WITH_SUBCOMMANDS, "asdf", "test") {
                override fun execute(context: CommandContext, vararg opt: Any): String {
                    println("asdf")
                    return "a"
                }
            }.addSubcommand(object : ICommand<String>("/", "subcommand", CommandDefaults.NO_DESCRIPTION, CommandDefaults.NO_USAGE, CommandDefaults.EXEC_WITH_SUBCOMMANDS, "asdf", "test") {
                override fun execute(context: CommandContext, vararg opt: Any): String {
                    println("asdf v2")
                    return "a"
                }
            }).addSubcommand(object : ICommand<String>("/", "subcommand2", CommandDefaults.NO_DESCRIPTION, CommandDefaults.NO_USAGE, CommandDefaults.EXEC_WITH_SUBCOMMANDS, "asdf2", "test2") {
                override fun execute(context: CommandContext, vararg opt: Any): String {
                    println("asdf v2")
                    return "a"
                }
            }).addSubcommand(object : ICommand<String>("/", "subcommand3", CommandDefaults.NO_DESCRIPTION, CommandDefaults.NO_USAGE, CommandDefaults.EXEC_WITH_SUBCOMMANDS, "asdf3", "test3") {
                override fun execute(context: CommandContext, vararg opt: Any): String {
                    println("asdf v2")
                    return "a"
                }
            }))
        }
    }

    @Test
    fun test() {
        kommandant.process<Any>("/asdf asd")
    }
}