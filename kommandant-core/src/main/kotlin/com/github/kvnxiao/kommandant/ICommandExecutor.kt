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

/**
 * An interface that defines the methods a command executor needs.
 */
interface ICommandExecutor {

    /**
     * Executes the command with the provided command [context][CommandContext] and any optional data.
     *
     * @param[command] The command to execute.
     * @param[context] The context of the command, containing the calling alias and any args it may have.
     * @param[opt] A nullable vararg of [Any], which can be useful in specific implementations when a command requires
     * more than just context for execution.
     * @return[CommandResult] The result after command execution.
     */
    fun <T> execute(command: ICommand<*>, context: CommandContext, vararg opt: Any?): CommandResult<T>

    /**
     * Method executed when a command is successfully executed.
     *
     * @param[context] The context of the command, containing the calling alias and any args it may have.
     * @param[opt] A nullable vararg of [Any] (any object)
     */
    fun onCommandExecute(context: CommandContext, vararg opt: Any?)

    /**
     * Method executed when an attempt to execute a disabled command was made.
     *
     * @param[context] The context of the command, containing the calling alias and any args it may have.
     * @param[opt] A nullable vararg of [Any] (any object)
     */
    fun onCommandExecuteDisabled(context: CommandContext, vararg opt: Any?)

    /**
     * Method executed when a parent command to execute was skipped directly to the subcommand.
     *
     * @param[context] The context of the command, containing the calling alias and any args it may have.
     * @param[opt] A nullable vararg of [Any] (any object)
     */
    fun onCommandExecuteSkipped(context: CommandContext, vararg opt: Any?)
}