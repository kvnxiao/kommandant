package testcommands

import com.github.kvnxiao.kommandant.command.CommandAnn
import com.github.kvnxiao.kommandant.command.CommandContext

/**
 * Created by kxiao on 3/8/17.
 */
class EnabledDisabledCommands {

    @CommandAnn(
            uniqueName = "enabled",
            aliases = arrayOf("enabled"),
            isDisabled = false
    )
    fun enabled(context: CommandContext, vararg opt: Any): String {
        return "This command is enabled"
    }

    @CommandAnn(
            uniqueName = "disabled",
            aliases = arrayOf("disabled"),
            isDisabled = true
    )
    fun disabled(context: CommandContext, vararg opt: Any): String {
        return "This command is disabled"
    }

    @CommandAnn(
            uniqueName = "parentenabled",
            aliases = arrayOf("parent"),
            isDisabled = false
    )
    fun parentEnabled(context: CommandContext, vararg opt: Any): String {
        return "This main command is enabled"
    }

    @CommandAnn(
            uniqueName = "childdisabled",
            aliases = arrayOf("child"),
            isDisabled = true,
            parentName = "parentenabled"
    )
    fun childDisabled(context: CommandContext, vararg opt: Any): String {
        return "This sub command is disabled"
    }

}