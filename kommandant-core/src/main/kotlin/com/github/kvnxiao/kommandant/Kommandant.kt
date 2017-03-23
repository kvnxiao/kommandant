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
package com.github.kvnxiao.kommandant

import com.github.kvnxiao.kommandant.command.CommandContext
import com.github.kvnxiao.kommandant.command.CommandResult
import com.github.kvnxiao.kommandant.command.ICommand
import com.github.kvnxiao.kommandant.impl.CommandBank
import com.github.kvnxiao.kommandant.impl.CommandExecutor
import com.github.kvnxiao.kommandant.impl.CommandParser
import com.github.kvnxiao.kommandant.utility.ImmutableCommandMap
import com.github.kvnxiao.kommandant.utility.SplitString
import com.github.kvnxiao.kommandant.utility.StringSplitter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass

/**
 * The default aggregation of a command registry, parser, and executor, which manages and executes commands.
 *
 * @property[cmdBank] The command registry, an implementation of [ICommandBank].
 * @property[cmdExecutor] The command executor, an implementation of [ICommandExecutor].
 * @property[cmdParser] The command parser, an implementation of [ICommandParser].
 * @constructor Default constructor which uses default implementations of the registry, parser, and executor.
 */
open class Kommandant(protected val cmdBank: ICommandBank = CommandBank(), protected val cmdExecutor: ICommandExecutor = CommandExecutor(), protected val cmdParser: ICommandParser = CommandParser()) {

    companion object {
        /**
         * Universal logger for logging Kommandant events. For example, registering a command, executing a command, etc.
         */
        @JvmField
        val LOGGER: Logger = LoggerFactory.getLogger(Kommandant::class.java)
    }

    /**
     * Processes a string input with any additional variables for command execution.
     *
     * @param[input] The string input to parse into valid context and command.
     * @param[opt] A nullable vararg of optional, extra input variables.
     * @return[CommandResult] The result after attempting execution of the command. Will return false and null if there
     * was no command to execute.
     */
    open fun <T> process(input: String, vararg opt: Any?): CommandResult<T> {
        val (alias, args) = SplitString(input)

        if (alias !== null) {
            val command: ICommand<*>? = this.getCommand(alias)
            if (command !== null) {
                val context = CommandContext(alias, args, command)
                return processCommand(command, context, *opt)
            }
        }
        return CommandResult(false)
    }

    open fun splitString(input: String): Array<String> = StringSplitter.split(input, StringSplitter.SPACE_LITERAL, 2)

    /**
     * Processes a provided command with a given command context and any additional variables for command execution.
     *
     * @param[command] The command to execute.
     * @param[context] The context of the command, containing the calling alias and any args it may have.
     * @param[opt] A nullable vararg of [Any], which can be useful in specific implementations when a command requires
     * more than just context for execution.
     * @return[CommandResult] The result after attempting execution of the command. Will return false and null if there
     * was no command to execute.
     */
    open protected fun <T> processCommand(command: ICommand<*>, context: CommandContext, vararg opt: Any?): CommandResult<T> = cmdExecutor.execute(command, context, *opt)

    /* * *
    *
    *   Wrapper functions for the command bank, parser, and executor
    *
    * * */

    /**
     * Deletes all commands from the registry and clears the registry's internal store.
     *
     * @see[CommandBank.clearAll]
     */
    open fun clearAll() = cmdBank.clearAll()

    /**
     * Adds a [command] to the registry. Failure to add commands occur if an alias clashes with another command's alias
     * for the provided prefix, or if the command's identifier / name is not unique.
     *
     * @see[CommandBank.addCommand]
     */
    open fun addCommand(command: ICommand<*>): Boolean = cmdBank.addCommand(command)

    /**
     * Gets a command by providing a single string representing the combination of the command's prefix and alias.
     *
     * @see[CommandBank.getCommand]
     */
    open fun getCommand(prefixedAlias: String): ICommand<*>? = cmdBank.getCommand(prefixedAlias)

    /**
     * Deletes a command from the registry by removing it and unlinking all subcommand references.
     *
     * @see[CommandBank.deleteCommand]
     */
    open fun deleteCommand(command: ICommand<*>?): Boolean = if (command !== null) cmdBank.deleteCommand(command) else false

    /**
     * Changes a prefix of a command in the registry.
     *
     * @see[CommandBank.changePrefix]
     */
    open fun changePrefix(command: ICommand<*>?, newPrefix: String): Boolean = if (command !== null) cmdBank.changePrefix(command, newPrefix) else false

    /**
     * Gets all the prefixes currently registered.
     *
     * @see[CommandBank.getPrefixes]
     */
    open fun getPrefixes(): Set<String> = cmdBank.getPrefixes()

    /**
     * Gets all the 'aliases' to 'command' map currently registered under the specified prefix.
     *
     * @see[CommandBank.getCommandsForPrefix]
     */
    open fun getCommandsForPrefix(prefix: String): ImmutableCommandMap = cmdBank.getCommandsForPrefix(prefix)

    /**
     * Gets all the 'prefix + aliases' to 'command' map from the registry.
     *
     * @see[CommandBank.getCommands]
     */
    open fun getAllCommands(): ImmutableCommandMap = cmdBank.getCommands()

    /**
     * Gets an immutable list of all registered commands in the registry.
     *
     * @see[CommandBank.getCommandsUnique]
     */
    open fun getCommandsUnique(): List<ICommand<*>> = cmdBank.getCommandsUnique()

    /**
     * Parse [CommandAnn][com.github.kvnxiao.kommandant.command.CommandAnn] annotations for a list of commands created from those annotations.
     *
     * @see[CommandParser.parseAnnotations]
     */
    open fun parseAnnotations(instance: Any): List<ICommand<*>> = cmdParser.parseAnnotations(instance)

    /**
     * Parses a provided object instance for annotated commands in its class and adds them all to the command registry.
     */
    open fun addAnnotatedCommands(instance: Any) {
        try {
            // Add each command resulting from parsed annotations to the bank
            this.parseAnnotations(instance).forEach {
                this.addCommand(it)
            }
        } catch (e: InvocationTargetException) {
            LOGGER.error("'${e.localizedMessage}': Could not instantiate an object instance of class '${instance::class.java.name}'!")
        } catch (e: IllegalAccessException) {
            LOGGER.error("'${e.localizedMessage}': Failed to access method definition in class '${instance::class.java.name}'!")
        }
    }

    /**
     * Parses a provided (Java) class for annotated commands and adds them all to the command registry.
     */
    open fun addAnnotatedCommands(clazz: Class<*>) = this.addAnnotatedCommands(clazz.newInstance())

    /**
     * Parses a provided (Kotlin) class for annotated commands and adds them all to the command registry.
     */
    open fun addAnnotatedCommands(ktClazz: KClass<*>) = this.addAnnotatedCommands(ktClazz.java.newInstance())

}