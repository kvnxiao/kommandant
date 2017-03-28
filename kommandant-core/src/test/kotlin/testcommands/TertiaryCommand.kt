package testcommands

import com.github.kvnxiao.kommandant.command.CommandAnn
import com.github.kvnxiao.kommandant.command.CommandContext

class TertiaryCommand {

    @CommandAnn(uniqueName = "primary", aliases = arrayOf("primary"))
    fun mainCommand(context: CommandContext, vararg opt: Any?): String {
        return "this is a primary command!"
    }

    @CommandAnn(uniqueName = "secondary", aliases = arrayOf("secondary"), parentName = "primary")
    fun subCommand(context: CommandContext, vararg opt: Any?): String {
        return "this is a secondary command!"
    }

    @CommandAnn(uniqueName = "tertiary", aliases = arrayOf("tertiary"), parentName = "secondary")
    fun tertiaryCommand(context: CommandContext, vararg opt: Any?): String {
        return "this is a tertiary command!"
    }

    @CommandAnn(uniqueName = "quaternary", aliases = arrayOf("quaternary", "quad"), parentName = "tertiary")
    fun quaternaryCommand(context: CommandContext, vararg opt: Any?): String {
        return "this is a quaternary command!"
    }

}