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

import com.github.kvnxiao.kommandant.command.CommandDefaults
import com.github.kvnxiao.kommandant.command.CommandPackage
import com.github.kvnxiao.kommandant.command.CommandProperties
import com.github.kvnxiao.kommandant.command.annotations.Command
import com.github.kvnxiao.kommandant.command.annotations.CommandGroup
import com.github.kvnxiao.kommandant.command.annotations.CommandInfo
import com.github.kvnxiao.kommandant.command.annotations.Prefix
import java.lang.reflect.Method

open class AnnotationParserImpl : AnnotationParser {
    override fun parseAnnotations(instance: Any): List<CommandPackage<*>> {
        val clazz = instance::class.java

        val subCommands: MutableSet<CommandPackage<*>> = mutableSetOf()
        val commands: MutableMap<String, CommandPackage<*>> = mutableMapOf()
        val mainCommands: MutableSet<CommandPackage<*>> = mutableSetOf()

        // Create and add command to registry for each executable method marked with @Command annotation
        val commandGroup: CommandGroup? = instance::class.java.getCommandGroup()
        val globalPrefix: String? = instance::class.java.getGlobalPrefix()

        clazz.methods
            .filter { it.isAnnotationPresent(Command::class.java) }
            .forEach {
                val commandAnn: Command = it.getAnnotation(Command::class.java)

                // Create command to add to bank
                val commandPackage: CommandPackage<*> =
                    this.createCommand(instance, it, commandAnn, commandGroup, globalPrefix)
            }
        TODO()
    }

    protected open fun createCommand(instance: Any, method: Method, commandAnn: Command, commandGroup: CommandGroup?, globalPrefix: String?): CommandPackage<*> {
        TODO()
    }

    protected open fun createProperties(id: String, prefix: String, parentId: String, commandAnn: Command, commandInfo: CommandInfo?): CommandProperties {
        return CommandProperties(
            id = id,
            prefix = prefix,
            aliases = commandAnn.aliases.toSet(),
            parentId = parentId,
            description = commandInfo?.description ?: CommandDefaults.NO_DESCRIPTION,
            usage = commandInfo?.usage ?: CommandDefaults.NO_USAGE,
            execWithSubCommands = CommandDefaults.EXEC_WITH_SUBCOMMANDS,
            isDisabled = CommandDefaults.IS_DISABLED
        )
    }

    protected fun Class<out Any>.getCommandGroup(): CommandGroup? =
        if (this.isAnnotationPresent(CommandGroup::class.java)) this.getAnnotation(CommandGroup::class.java)
        else null

    protected fun Class<out Any>.getGlobalPrefix(): String? =
        if (this.isAnnotationPresent(Prefix::class.java)) this.getAnnotation(Prefix::class.java).prefix
        else null

    protected fun Method.getPrefix(): Prefix? =
        if (this.isAnnotationPresent(CommandGroup::class.java)) this.getAnnotation(Prefix::class.java)
        else null
}
