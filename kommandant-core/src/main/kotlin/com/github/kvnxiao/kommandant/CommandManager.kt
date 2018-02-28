/*
 *   Copyright (C) 2017-2018 Ze Hao Xiao
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing commandSettings and
 *   limitations under the License.
 */
package com.github.kvnxiao.kommandant

import arrow.core.Either
import com.github.kvnxiao.kommandant.command.CommandPackage
import com.github.kvnxiao.kommandant.command.Context
import java.util.concurrent.Future

/**
 * Describes a command manager, which is capable of processing input strings to retrieve and execute a command contained
 * in the registry, as well as being able to add commands and sub-commands to the registry.
 *
 * @see Kommandant The default implementation of a command manager.
 */
interface CommandManager {

    /**
     * Asynchronously processes an input string for potential commands (all possible command prefixes),
     * and executes it if the command exists.
     *
     * Creates a [Context] and supplies it to [execute] if the command exists.
     *
     * @param opt the optional array of extra arguments to supply to the command during execution.
     */
    fun <T> processAsync(input: String, opt: Array<Any>? = null): Future<Either<Exception, T>>

    /**
     * Synchronously processes an input string for potential commands (all possible command prefixes),
     * and executes it if the command exists.
     *
     * Creates a [Context] and supplies it to [execute] if the command exists.
     *
     * @param opt the optional array of extra arguments to supply to the command during execution.
     */
    fun <T> process(input: String, opt: Array<Any>? = null): Either<Exception, T> = processAsync<T>(input, opt).get()

    /**
     * Executes the supplied command with the given context and optional array of extra arguments.
     */
    fun <T> execute(command: CommandPackage<*>, context: Context, opt: Array<Any>? = null): Future<Either<Exception, T>>

    /**
     * Parses class instances for annotated command annotations to create and add annotated commands into the registry.
     *
     * @return true on successful parsing of the supplied classes into annotated commands.
     */
    fun addAnnotatedCommands(vararg instances: Any): Boolean

    /**
     * Adds a command to the registry.
     *
     * @return true on successfully adding the command to the registry without conflict.
     */
    fun addCommand(command: CommandPackage<*>): Boolean

    /**
     * Adds a sub-command to the registry, by specifying the unique id of the parent command.
     *
     * @return true on successfully adding the command to the registry without conflict.
     */
    fun addSubCommand(subCommand: CommandPackage<*>, parentId: String): Boolean
}
