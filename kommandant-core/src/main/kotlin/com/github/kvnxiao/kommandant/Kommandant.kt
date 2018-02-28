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
import com.github.kvnxiao.kommandant.command.CommandDefaults
import com.github.kvnxiao.kommandant.command.CommandPackage
import com.github.kvnxiao.kommandant.command.Context
import com.github.kvnxiao.kommandant.command.errors.CommandNotFoundException
import com.github.kvnxiao.kommandant.command.executor.CommandExecutor
import com.github.kvnxiao.kommandant.command.executor.CommandExecutorImpl
import com.github.kvnxiao.kommandant.command.parser.AnnotationParser
import com.github.kvnxiao.kommandant.command.parser.AnnotationParserImpl
import com.github.kvnxiao.kommandant.command.registry.CommandRegistry
import com.github.kvnxiao.kommandant.command.registry.CommandRegistryImpl
import com.github.kvnxiao.kommandant.utility.SplitString
import mu.KotlinLogging
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.ScheduledExecutorService

/**
 * The default command manager implementation.
 * Capable of adding commands, processing input strings, and executing commands.
 *
 * @see CommandManager
 */
open class Kommandant(
    /**
     * The command registry for adding, retrieving, and removing commands.
     */
    protected val registry: CommandRegistry = CommandRegistryImpl(),
    /**
     * The executor processor for executing commands and catching errors to forward to the error handlers.
     */
    protected val executor: CommandExecutor = CommandExecutorImpl(),
    /**
     * The annotation parser for creating annotation based commands.
     */
    protected val annotationParser: AnnotationParser = AnnotationParserImpl(),
    /**
     * Scheduled thread pool for executing multiple commands asynchronously.
     */
    protected val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(2)
) : CommandManager {

    companion object {
        @JvmStatic
        protected val LOGGER = KotlinLogging.logger(Kommandant::class.java.name)
    }

    override fun <T> processAsync(input: String, opt: Array<Any>?): Future<Either<Exception, T>> {
        LOGGER.debug { "Processing input string: $input" }
        val (alias, args) = SplitString(input)
        val command = registry.getCommandByAlias(alias)
        return if (command != null) {
            val context = Context(alias, args, command.properties)
            this.processNext(command, context, opt)
        } else {
            CompletableFuture.completedFuture(Either.left(CommandNotFoundException(alias)))
        }
    }

    /**
     * Called by [processAsync] to check the input string for potential sub-commands to execute.
     */
    protected fun <T> processNext(command: CommandPackage<*>, context: Context, opt: Array<Any>?): Future<Either<Exception, T>> {
        // Check sub-commands
        val args = context.args
        if (args != null && registry.hasSubCommands(context.properties.id)) {
            // Try getting a sub-command from the arguments
            val (subAlias, subArgs) = SplitString(args)
            val subCommand = registry.getSubCommandByAlias(subAlias, context.properties.id)
            subCommand?.let {
                // Create new command context for sub-command
                val subContext = Context(subAlias, subArgs, it.properties)
                // Execute parent command if the execWithSubCommands value is set to true
                if (context.properties.execWithSubCommands) command.executable.execute(context, opt)
                return processNext(subCommand, subContext, opt)
            }
        }
        return execute(command, context, opt)
    }

    override fun <T> execute(command: CommandPackage<*>, context: Context, opt: Array<Any>?): Future<Either<Exception, T>> {
        return scheduler.submit<Either<Exception, T>> {
            executor.execute(command, context, opt)
        }
    }

    override fun addAnnotatedCommands(vararg instances: Any): Boolean {
        // Get commands created by annotation parser
        val list = instances.flatMap { annotationParser.parseAnnotations(it) }
        if (list.isEmpty()) return false
        // Validate commands before adding to registry
        return if (validate(list)) {
            list.forEach {
                // Register root-level commands
                if (it.properties.parentId == CommandDefaults.PARENT_ID) {
                    this.addCommand(it)
                } else {
                    // Register sub-commands
                    this.addSubCommand(it, it.properties.parentId)
                }
            }
            true
        } else {
            false
        }
    }

    /**
     * Validates a list of commands for insertion into the registry. Checks for conflicting unique ids and aliases
     * between each command, as well as within the registry.
     */
    protected fun validate(commands: List<CommandPackage<*>>): Boolean {
        val aliasList = commands.flatMap { pkg -> pkg.properties.aliases.map { pkg.properties.prefix + it } }
        val idList = commands.map { it.properties.id }
        when {
            idList.toSet().size != idList.size -> {
                LOGGER.error { "Some of the annotated commands have conflicting unique ids." }
                return false
            }
            aliasList.toSet().size != aliasList.size -> {
                LOGGER.error { "Some of the annotated commands have conflicting aliases between one another." }
                return false
            }
            !commands.all {
                registry.validateAliases(it.properties.prefix, it.properties.aliases)
                    && registry.validateUniqueId(it.properties.id)
            } -> {
                LOGGER.error { "Some of the annotated commands have conflicting aliases or unique ids with the current registry." }
                return false
            }
            else -> return true
        }
    }

    override fun addCommand(command: CommandPackage<*>): Boolean = registry.addCommand(command)

    override fun addSubCommand(subCommand: CommandPackage<*>, parentId: String): Boolean = registry.addSubCommand(subCommand, parentId)
}
