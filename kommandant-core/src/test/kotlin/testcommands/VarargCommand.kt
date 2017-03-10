package testcommands

import com.github.kvnxiao.kommandant.command.CommandAnn
import com.github.kvnxiao.kommandant.command.CommandContext

/**
 * Created by kxiao on 3/10/17.
 */
class VarargCommand {

    @CommandAnn(
            uniqueName = "varargannotation",
            aliases = arrayOf("annotation")
    )
    fun varargs(context: CommandContext, vararg opt: Any?): Unit {
        opt.forEach(::println)
    }

}