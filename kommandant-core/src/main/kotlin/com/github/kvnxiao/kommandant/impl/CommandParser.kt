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
import java.util.Stack

/**
 * The default implementation of [ICommandParser]. Parses a class containing the [CommandAnn] annotation to create
 * commands from these annotations.
 */
open class CommandParser : ICommandParser {

    /**
     * Parse [CommandAnn] annotations in a class and returns a list of commands created from those annotations.
     *
     * @param[instance] The instance object for which its class is to be parsed.
     * @return[List] The list of commands created after parsing.
     */
    override fun parseAnnotations(instance: Any): List<ICommand<*>> {
        // Instantiate new instance of class to reference method invocations
        val clazz = instance::class.java

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

        // Link sub commands to parent
        for ((subCommand, parentName) in subCommands) {
            commands[parentName]?.addSubcommand(subCommand)
            LOGGER.debug("Registered command '${subCommand.props.uniqueName}' as a subcommand of parent '$parentName'")
        }

        // Clear utility containers as we are done adding all commands
        subCommands.clear()
        commands.clear()

        return mainCommands.toList()
    }

    /**
     * Creates a command by parsing a single [CommandAnn] annotation, with its execution method set as the method
     * targeted by the annotation.
     *
     * @param[instance] The instance object for which its class is to be parsed.
     * @param[method] The method to invoke for command execution.
     * @param[annotation] The annotation to parse.
     * @return[ICommand] A newly created command with properties taken from the annotation.
     */
    override fun createCommand(instance: Any, method: Method, annotation: CommandAnn): ICommand<Any?> {
        return object : ICommand<Any?>(annotation.prefix, annotation.uniqueName, annotation.description, annotation.usage, annotation.execWithSubcommands, annotation.isDisabled, *annotation.aliases) {
            @Throws(InvocationTargetException::class, IllegalAccessException::class)
            override fun execute(context: CommandContext, vararg opt: Any?): Any? {
                return method.invoke(instance, context, opt)
            }
        }
    }

}