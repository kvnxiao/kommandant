package com.github.kvnxiao.kommandant

import com.github.kvnxiao.kommandant.command.CommandContext
import com.github.kvnxiao.kommandant.command.ICommand
import com.github.kvnxiao.kommandant.command.Success

/**
 * Created on:   2017-03-05
 * Author:       Kevin Xiao (github.com/alphahelix00)
 *
 */
interface ICommandExecutor {

    fun <T> execute(command: ICommand<*>, context: CommandContext, success: Success, vararg opt: Any?): T?

}