package com.github.kvnxiao.kommandant

import com.github.kvnxiao.kommandant.command.CommandContext
import com.github.kvnxiao.kommandant.command.CommandResult
import com.github.kvnxiao.kommandant.command.ICommand

/**
 * An interface that defines the methods a command executor needs.
 */
interface ICommandExecutor {

    fun <T> execute(command: ICommand<*>, context: CommandContext, vararg opt: Any?): CommandResult<T>

}