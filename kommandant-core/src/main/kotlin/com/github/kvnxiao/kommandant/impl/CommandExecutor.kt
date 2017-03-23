/*
 * Copyright 2017 Ze Hao Xiao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.kvnxiao.kommandant.impl

import com.github.kvnxiao.kommandant.ICommandExecutor
import com.github.kvnxiao.kommandant.Kommandant.Companion.LOGGER
import com.github.kvnxiao.kommandant.command.CommandContext
import com.github.kvnxiao.kommandant.command.CommandResult
import com.github.kvnxiao.kommandant.command.ICommand
import com.github.kvnxiao.kommandant.utility.SplitString
import java.lang.reflect.InvocationTargetException

/**
 * The default implementation of [ICommandExecutor]. Executes a specified command by running the
 * [ICommandExecutable][com.github.kvnxiao.kommandant.command.ICommandExecutable] method.
 */
open class CommandExecutor : ICommandExecutor {

    /**
     * Executes the command with the provided command [context][CommandContext] and any optional data.
     *
     * @param[command] The command to execute.
     * @param[context] The context of the command, containing the calling alias and any args it may have.
     * @param[opt] A nullable vararg of [Any], which can be useful in specific implementations when a command requires
     * more than just context for execution.
     * @return[CommandResult] The result after command execution.
     */
    override fun <T> execute(command: ICommand<*>, context: CommandContext, vararg opt: Any?): CommandResult<T> {
        if (!command.props.disabled) {
            if (checkOtherSettings(command, context, *opt)) {
                try {
                    if (context.hasArgs() && command.hasSubcommands()) {
                        val (alias, args) = SplitString(context.args!!)

                        if (alias !== null) {
                            val subCommand: ICommand<*>? = command.subCommandMap[alias]
                            if (subCommand !== null) {
                                val subContext = CommandContext(alias, args, subCommand)

                                // Execute main command, then subcommand
                                if (command.props.execWithSubcommands) {
                                    onCommandExecute(context, *opt)
                                    executeCommand<T>(command, context, *opt)
                                } else {
                                    onCommandExecuteSkipped(context, *opt)
                                }
                                return execute(subCommand, subContext, *opt)
                            }
                        }
                    }
                    onCommandExecute(context, *opt)
                    return CommandResult(true, executeCommand(command, context, *opt))
                } catch (e: InvocationTargetException) {
                    LOGGER.error("${e.localizedMessage}: Failed to invoke method bound to command '${command.props.uniqueName}'!")
                } catch (e: IllegalAccessException) {
                    LOGGER.error("${e.localizedMessage}: Failed to access method definition for command '${command.props.uniqueName}'!")
                }
            }
        } else {
            onCommandExecuteDisabled(context, *opt)
        }
        return CommandResult(false)
    }

    /**
     * Runs the command's execution method defined by its [ICommandExecutable][com.github.kvnxiao.kommandant.command.ICommandExecutable].
     *
     * Because the command in the parameter can have a method return of any generic type, we require an unchecked cast
     * to convert from a wildcard to specific type T. We operate under assumption that the user knows what they are
     * doing and that the command should have a specific return type of type T.
     *
     * @param[command] The command to execute.
     * @param[context] The context of the command, containing the calling alias and any args it may have.
     * @param[opt] A nullable vararg of [Any], which can be useful in specific implementations when a command requires
     * more than just context for execution.
     * @throws[InvocationTargetException] When the method invocation failed.
     * @throws[IllegalAccessException] When the method could not be accessed through reflection.
     * @return[T] Returns the result of the execution.
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(InvocationTargetException::class, IllegalAccessException::class)
    open protected fun <T> executeCommand(command: ICommand<*>, context: CommandContext, vararg opt: Any?): T {
        return command.execute(context, *opt) as T
    }

    /**
     * An extra method that can be used in other implementations to check whether it is valid to execute the command.
     * Akin to a "permission" check.
     *
     * @param[command] The command to execute.
     * @param[context] The context of the command, containing the calling alias and any args it may have.
     * @param[opt] A nullable vararg of [Any], which can be useful in specific implementations when a command requires
     * more than just context for execution.
     * @return[Boolean] Defaults to true in the core library. This method can be overrided in other project implementations.
     */
    open protected fun checkOtherSettings(command: ICommand<*>, context: CommandContext, vararg opt: Any?): Boolean = true

    /**
     * Method executed when a command is successfully executed. By default, this simply logs the command being executed.
     *
     * @param[context] The context of the command, containing the calling alias and any args it may have.
     * @param[opt] A nullable vararg of [Any] (any object)
     */
    override fun onCommandExecute(context: CommandContext, vararg opt: Any?) {
        LOGGER.debug("Executing command '${context.properties.uniqueName}' with args: [${if (context.hasArgs()) context.args else " "}]")
    }

    /**
     * Method executed when an attempt to execute a disabled command was made. By default, this simply logs the attempt.
     *
     * @param[context] The context of the command, containing the calling alias and any args it may have.
     * @param[opt] A nullable vararg of [Any] (any object)
     */
    override fun onCommandExecuteDisabled(context: CommandContext, vararg opt: Any?) {
        LOGGER.debug("Executing command '${context.properties.uniqueName}' ignored because the command is disabled.")
    }

    /**
     * Method executed when a parent command to execute was skipped directly to the subcommand.
     * By default, this simply logs the parent command that was skipped.
     *
     * @param[context] The context of the command, containing the calling alias and any args it may have.
     * @param[opt] A nullable vararg of [Any] (any object)
     */
    override fun onCommandExecuteSkipped(context: CommandContext, vararg opt: Any?) {
        LOGGER.debug("Executing command '${context.properties.uniqueName}' with args: [${if (context.hasArgs()) context.args else " "}] SKIPPED because execWithSubcommands is set to false")
    }
}