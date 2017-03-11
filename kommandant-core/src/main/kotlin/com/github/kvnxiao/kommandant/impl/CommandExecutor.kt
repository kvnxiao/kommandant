package com.github.kvnxiao.kommandant.impl

import com.github.kvnxiao.kommandant.ICommandExecutor
import com.github.kvnxiao.kommandant.Kommandant.Companion.LOGGER
import com.github.kvnxiao.kommandant.command.CommandContext
import com.github.kvnxiao.kommandant.command.CommandResult
import com.github.kvnxiao.kommandant.command.ICommand
import java.lang.reflect.InvocationTargetException

/**
 * Created by kvnxiao on 3/3/17.
 */
open class CommandExecutor : ICommandExecutor {

    override fun <T> execute(command: ICommand<*>, context: CommandContext, vararg opt: Any?): CommandResult<T> {
        if (!command.props.isDisabled && checkOtherSettings(command, context, opt)) {
            try {
                if (context.hasArgs() && command.hasSubcommands()) {
                    val subContext: CommandContext = CommandContext(context.args)
                    val subCommand: ICommand<*>? = command.subCommandMap[subContext.alias]
                    if (subContext.hasAlias() && subCommand !== null) {
                        // Execute main command, then subcommand
                        if (command.props.execWithSubcommands) {
                            LOGGER.debug("Executing command ${command.props.uniqueName} with args: [${if (context.hasArgs()) context.args else " "}]")
                            executeCommand<T>(command, context, *opt)
                        } else {
                            LOGGER.debug("Executing command ${command.props.uniqueName} with args: [${if (context.hasArgs()) context.args else " "}], skipping result because execWithSubcommands is set to false")
                        }
                        return execute(subCommand, subContext, *opt)
                    }
                }
                LOGGER.debug("Executing command ${command.props.uniqueName} with args: [${if (context.hasArgs()) context.args else " "}]")
                return CommandResult(true, executeCommand(command, context, *opt))
            } catch (e: InvocationTargetException) {
                LOGGER.error("${e.localizedMessage}: Failed to invoke method bound to command '${command.props.uniqueName}'!")
            } catch (e: IllegalAccessException) {
                LOGGER.error("${e.localizedMessage}: Failed to access method definition for command '${command.props.uniqueName}'!")
            }
        }
        LOGGER.debug("Executing command '${command.props.uniqueName}' ignored because the command is disabled.")
        return CommandResult(false)
    }

    // Because the command in the parameter can have a method return of any type '<*>',
    // We require an unchecked cast to convert from wildcard <*> to specific type T.
    // We operate under assumption that the user knows what they are doing and that
    // The command should have a specific return type of type T.
    @Suppress("UNCHECKED_CAST")
    @Throws(InvocationTargetException::class, IllegalAccessException::class)
    open protected fun <T> executeCommand(command: ICommand<*>, context: CommandContext, vararg opt: Any?): T {
        return command.execute(context, *opt) as T
    }

    open protected fun checkOtherSettings(command: ICommand<*>, context: CommandContext, vararg opt: Any?): Boolean = true

}