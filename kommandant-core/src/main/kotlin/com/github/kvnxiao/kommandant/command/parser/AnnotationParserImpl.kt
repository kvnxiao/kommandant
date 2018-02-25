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
package com.github.kvnxiao.kommandant.command.parser

import com.github.kvnxiao.kommandant.DefaultErrorHandler
import com.github.kvnxiao.kommandant.command.CommandDefaults
import com.github.kvnxiao.kommandant.command.CommandErrorHandler
import com.github.kvnxiao.kommandant.command.CommandExecutable
import com.github.kvnxiao.kommandant.command.CommandPackage
import com.github.kvnxiao.kommandant.command.CommandProperties
import com.github.kvnxiao.kommandant.command.Context
import com.github.kvnxiao.kommandant.command.annotations.Command
import com.github.kvnxiao.kommandant.command.annotations.CommandGroup
import com.github.kvnxiao.kommandant.command.annotations.CommandInfo
import com.github.kvnxiao.kommandant.command.annotations.CommandSettings
import com.github.kvnxiao.kommandant.command.annotations.Prefix
import com.github.kvnxiao.kommandant.utility.LINE_SEPARATOR
import mu.KotlinLogging
import java.lang.reflect.Method
import java.util.ArrayDeque
import java.util.Queue

private val LOGGER = KotlinLogging.logger { }

open class AnnotationParserImpl : AnnotationParser {
    override fun parseAnnotations(instance: Any): List<CommandPackage<*>> {
        val clazz = instance::class.java

        // Preconditions check
        val methodSet = clazz.methods.filter { it.isAnnotationPresent(Command::class.java) }.toMutableSet()
        if (methodSet.isEmpty()) {
            LOGGER.debug { "Skipping class ${clazz.name} as there are no methods annotated with @${Command::class.java.simpleName}" }
            return emptyList()
        }
        require(methodSet.map { it.commandAnn().id }.toSet().size == methodSet.size, {
            "All methods annotated with @${Command::class.java.simpleName} must each have a unique 'id' value!"
        })
        require(methodSet.none {
            val commandAnn = it.commandAnn()
            commandAnn.parentId == commandAnn.id
        }, {
            "Methods annotated with @${Command::class.java.simpleName} cannot self reference with the same 'id' and 'parentId' values."
        })

        // Establish parent-child relationship of methods
        val idToMethodMap = mutableMapOf<String, Method>()
        val methodToIdMap = mutableMapOf<Method, String>()
        val rootMethodSet = methodSet.filter { it.commandAnn().parentId == CommandDefaults.PARENT_ID }
        methodSet.removeAll(rootMethodSet)
        val methodQueue: Queue<Method> = ArrayDeque()
        methodQueue.addAll(rootMethodSet)
        while (methodQueue.isNotEmpty()) {
            // Add method to map
            val method = methodQueue.poll()
            val annId = method.commandAnn().id
            if (!methodToIdMap.containsKey(method)) {
                idToMethodMap[annId] = method
                methodToIdMap[method] = annId
            }
            // Find children of current method
            val childrenSet = methodSet.filter { it.commandAnn().parentId == annId }
            if (childrenSet.isNotEmpty()) {
                childrenSet.forEach {
                    val childId = methodToIdMap[method] + "." + it.commandAnn().id
                    idToMethodMap[childId] = it
                    methodToIdMap[it] = childId
                }
                methodQueue.addAll(childrenSet)
                methodSet.removeAll(childrenSet)
            }
        }
        require(methodSet.isEmpty(), {
            "${methodSet.size} invalid 'parentId' values declared in the @${Command::class.java.simpleName} annotation for ${instance::class.java.name}:" +
                methodSet.joinToString(separator = LINE_SEPARATOR, prefix = LINE_SEPARATOR) {
                    "method ${it.name} -> @${Command::class.java.simpleName} annotation id=${it.commandAnn().id} references invalid parentId=${it.commandAnn().parentId}"
                }
        })

        val commandGroup: String = instance::class.java.getCommandGroup() ?: CommandDefaults.PARENT_ID
        val globalPrefix: String? = instance::class.java.getGlobalPrefix()

        return idToMethodMap.map { (id, method) ->
            this.createCommand(method, method.commandAnn(), commandGroup + id, globalPrefix, instance)
        }.toList()
    }

    protected open fun createCommand(method: Method, commandAnn: Command, id: String, globalPrefix: String?, instance: Any): CommandPackage<*> {
        // Check if there is a local prefix -- Global prefix will always override local prefix -- Default to empty string ""
        val prefix = globalPrefix ?: method.getPrefix() ?: CommandDefaults.NO_PREFIX
        // Check if there is a @CommandInfo annotation
        val commandInfo = method.getCommandInfo()
        // Check if there is a @CommandSettings annotation
        val commandSettings = method.getCommandSettings()

        val properties = createProperties(
            id = id,
            prefix = prefix,
            newParentId = if (commandAnn.parentId == CommandDefaults.PARENT_ID) CommandDefaults.PARENT_ID else id.substring(0, id.length - commandAnn.id.length - 1),
            commandAnn = commandAnn,
            commandInfo = commandInfo,
            commandSettings = commandSettings
        )
        val executable = object : CommandExecutable<Any?> {
            override fun execute(context: Context, opt: Array<Any>?): Any? {
                return method.invoke(instance, context, opt)
            }
        }
        val errorHandler: CommandErrorHandler = DefaultErrorHandler()
        return CommandPackage(executable, properties, errorHandler)
    }

    protected open fun createProperties(id: String, prefix: String, newParentId: String,
                                        commandAnn: Command, commandInfo: CommandInfo?, commandSettings: CommandSettings?): CommandProperties {
        return CommandProperties(
            id = id,
            prefix = prefix,
            aliases = commandAnn.aliases.toSet(),
            parentId = newParentId,
            description = commandInfo?.description ?: CommandDefaults.NO_DESCRIPTION,
            usage = commandInfo?.usage ?: CommandDefaults.NO_USAGE,
            execWithSubCommands = CommandDefaults.EXEC_WITH_SUBCOMMANDS,
            isDisabled = CommandDefaults.IS_DISABLED
        )
    }

    protected fun Class<out Any>.getCommandGroup(): String? =
        if (this.isAnnotationPresent(CommandGroup::class.java)) this.getAnnotation(CommandGroup::class.java).groupName + "."
        else null

    protected fun Class<out Any>.getGlobalPrefix(): String? =
        if (this.isAnnotationPresent(Prefix::class.java)) this.getAnnotation(Prefix::class.java).prefix
        else null

    protected fun Method.getPrefix(): String? =
        if (this.isAnnotationPresent(Prefix::class.java)) this.getAnnotation(Prefix::class.java).prefix
        else null

    protected fun Method.getCommandInfo(): CommandInfo? =
        if (this.isAnnotationPresent(CommandInfo::class.java)) this.getAnnotation(CommandInfo::class.java)
        else null

    protected fun Method.getCommandSettings(): CommandSettings? =
        if (this.isAnnotationPresent(CommandSettings::class.java)) this.getAnnotation(CommandSettings::class.java)
        else null

    protected fun Method.commandAnn(): Command = this.getAnnotation(Command::class.java)
}
