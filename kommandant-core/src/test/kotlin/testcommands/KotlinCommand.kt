package testcommands

import com.github.kvnxiao.kommandant.command.CommandAnn
import com.github.kvnxiao.kommandant.command.CommandContext

/**
 * Created on:   2017-03-05
 * Author:       Kevin Xiao (github.com/alphahelix00)
 *
 */
class KotlinCommand {

    @CommandAnn(uniqueName = "kotlin", aliases = arrayOf("kotlin"))
    fun simpleCommand(context: CommandContext, vararg opt: Any?): String {
        return "this is a kotlin based command!"
    }

}