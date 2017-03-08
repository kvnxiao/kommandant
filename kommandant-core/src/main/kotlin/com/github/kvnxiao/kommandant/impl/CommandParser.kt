package com.github.kvnxiao.kommandant.impl

import com.github.kvnxiao.kommandant.ICommandBank
import com.github.kvnxiao.kommandant.ICommandParser
import com.github.kvnxiao.kommandant.Kommandant.Companion.LOGGER
import com.github.kvnxiao.kommandant.command.CommandAnn
import com.github.kvnxiao.kommandant.command.CommandContext
import com.github.kvnxiao.kommandant.command.CommandDefaults
import com.github.kvnxiao.kommandant.command.ICommand
import com.github.kvnxiao.kommandant.utility.CommandMap
import com.github.kvnxiao.kommandant.utility.CommandStack
import com.github.kvnxiao.kommandant.utility.CommandStringMap
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.*

/**
 * Created on:   2017-03-05
 * Author:       Kevin Xiao (github.com/alphahelix00)
 *
 */
open class CommandParser : ICommandParser {

    @Throws(InvocationTargetException::class, IllegalAccessException::class)
    override fun parseAnnotations(clazz: Class<*>, cmdBank: ICommandBank) {
        // Instantiate new instance of class to reference method invocations
        val instance = clazz.newInstance()

        // Use hashmaps to link sub commands to parent commands
        val subCommands: CommandStringMap = mutableMapOf()
        val commands: CommandMap = mutableMapOf()
        val mainCommands: CommandStack = Stack()

        // Create and add command to bank for each methods with 'executable' signature type
        for (method in clazz.methods) {
            if (method.isAnnotationPresent(CommandAnn::class.java)) {
                val annotation: CommandAnn = method.getAnnotation(CommandAnn::class.java)

                // Create command to add to bank
                val command: ICommand<*> = this.createCommand(instance, method, annotation)

                // Add main commands and chainable main commands to command bank
                if (annotation.parentName != CommandDefaults.PARENT || annotation.parentName == annotation.uniqueName) {
                    subCommands.put(command, annotation.parentName)
                }
                if (annotation.parentName == CommandDefaults.PARENT || annotation.parentName == annotation.uniqueName) {
                    mainCommands.push(command)
                }
                commands.put(annotation.uniqueName, command)
            }
        }

        // Link sub commmands to parent
        for ((subCommand, parentName) in subCommands) {
            commands[parentName]?.addSubcommand(subCommand)
            LOGGER.debug("Registered command '${subCommand.props.uniqueName}' as a subcommand of parent '$parentName'")
        }

        // Add all main commands to the command bank
        while (mainCommands.isNotEmpty()) {
            cmdBank.addCommand(mainCommands.pop())
        }

        // Clear utility containers as we are done adding all commands
        subCommands.clear()
        commands.clear()
        mainCommands.clear()
    }

    override fun createCommand(instance: Any, method: Method, annotation: CommandAnn): ICommand<Any?> {
        return object : ICommand<Any?>(annotation.prefix, annotation.uniqueName, annotation.description, annotation.usage, annotation.execWithSubcommands, annotation.isDisabled, *annotation.aliases) {
            @Throws(InvocationTargetException::class, IllegalAccessException::class)
            override fun execute(context: CommandContext, vararg opt: Any): Any? {
                return method.invoke(instance, context, opt)
            }
        }
    }

}