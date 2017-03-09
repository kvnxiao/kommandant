package com.github.kvnxiao.kommandant

import com.github.kvnxiao.kommandant.command.CommandContext
import com.github.kvnxiao.kommandant.command.CommandResult
import com.github.kvnxiao.kommandant.command.ICommand
import com.github.kvnxiao.kommandant.impl.CommandBank
import com.github.kvnxiao.kommandant.impl.CommandExecutor
import com.github.kvnxiao.kommandant.impl.CommandParser
import com.github.kvnxiao.kommandant.utility.ImmutableCommandMap
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass

/**
 * Created by kvnxiao on 3/3/17.
 */
open class Kommandant(protected val cmdBank: ICommandBank = CommandBank(),
                      protected val cmdExecutor: ICommandExecutor = CommandExecutor(),
                      protected val cmdParser: ICommandParser = CommandParser()) {

    companion object {
        @JvmField
        val LOGGER: Logger = LoggerFactory.getLogger(Kommandant::class.java)
    }

    open fun <T> process(input: String, vararg opt: Any?): CommandResult<T> {
        val context = CommandContext(input)
        if (context.hasAlias()) {
            val command: ICommand<*>? = this.getCommand(context.alias)
            if (command !== null) return processCommand(command, context, opt)
        }
        return CommandResult(false)
    }

    open protected fun <T> processCommand(command: ICommand<*>, context: CommandContext, vararg opt: Any?): CommandResult<T> = cmdExecutor.execute(command, context, opt)

    /* * *
    *
    *   Wrapper functions for the command bank, parser, and executor
    *
    * * */

    open fun clearAll() = cmdBank.clearAll()

    open fun addCommand(command: ICommand<*>): Boolean = cmdBank.addCommand(command)

    open fun getCommand(prefixedAlias: String): ICommand<*>? = cmdBank.getCommand(prefixedAlias)

    open fun deleteCommand(command: ICommand<*>?): Boolean = if (command !== null) cmdBank.deleteCommand(command) else false

    open fun changePrefix(command: ICommand<*>?, newPrefix: String): Boolean = if (command !== null) cmdBank.changePrefix(command, newPrefix) else false

    open fun getPrefixes(): Set<String> = cmdBank.getPrefixes()

    open fun getCommandsForPrefix(prefix: String): ImmutableCommandMap = cmdBank.getCommandsForPrefix(prefix)

    open fun getAllCommands(): ImmutableCommandMap = cmdBank.getCommands()

    open fun getCommandsUnique(): List<ICommand<*>> = cmdBank.getCommandsUnique()

    open fun addAnnotatedCommands(clazz: Class<*>) {
        try {
            cmdParser.parseAnnotations(clazz, this.cmdBank)
        } catch (e: InvocationTargetException) {
            LOGGER.error("'${e.localizedMessage}': Could not instantiate an object instance of class '${clazz.name}'!")
        } catch (e: IllegalAccessException) {
            LOGGER.error("'${e.localizedMessage}': Failed to access method definition in class '${clazz.name}'!")
        }
    }

    open fun addAnnotatedCommands(ktClazz: KClass<*>) = this.addAnnotatedCommands(ktClazz.java)

}