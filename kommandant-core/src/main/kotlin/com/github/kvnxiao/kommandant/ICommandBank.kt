package com.github.kvnxiao.kommandant

import com.github.kvnxiao.kommandant.command.ICommand
import com.github.kvnxiao.kommandant.utility.ImmutableCommandMap

/**
 * Created on:   2017-03-05
 * Author:       Kevin Xiao (github.com/alphahelix00)
 *
 */
interface ICommandBank {

    fun addCommand(command: ICommand<*>): Boolean
    fun removeCommand(command: ICommand<*>): Boolean
    fun deleteCommand(command: ICommand<*>): Boolean
    fun getCommand(singleString: String): ICommand<*>?
    fun changePrefix(command: ICommand<*>, newPrefix: String): Boolean
    fun getPrefixes(): Set<String>
    fun getCommands(): ImmutableCommandMap
    fun getCommandsForPrefix(prefix: String): ImmutableCommandMap?

}