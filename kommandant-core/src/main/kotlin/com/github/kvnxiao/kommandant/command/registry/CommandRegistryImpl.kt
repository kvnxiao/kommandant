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
package com.github.kvnxiao.kommandant.command.registry

import com.github.kvnxiao.kommandant.command.CommandDefaults
import com.github.kvnxiao.kommandant.command.CommandPackage
import mu.KotlinLogging

private val LOGGER = KotlinLogging.logger { }

open class CommandRegistryImpl : CommandRegistry() {

    /**
     * The map of unique command identifiers to the respective root-level commands.
     */
    protected val idCommandMap: MutableMap<String, CommandPackage<*>> = mutableMapOf()

    /**
     * The root-level map of command aliases to their respective commands (does not contain sub-commands).
     */
    protected val aliasIdMap: MutableMap<String, String> = mutableMapOf()

    /**
     * The map of parent commands to their respective sub-command registries.
     * The parent command can be either a root-level command, a sub-command of a root-level command, or a sub-command
     * of a sub-command, etc.
     */
    protected val parentIdSubCommandsMap: MutableMap<String, SubCommandRegistry> = mutableMapOf()

    /**
     * Ensure that before adding a new command, none of the prefix + alias combinations already exist in the registry
     */
    override fun validateAliases(prefix: String, aliases: Set<String>): Boolean {
        return aliases.none { aliasIdMap.containsKey(prefix + it) }
    }

    /**
     * Ensure that before adding a new command, the unique identifier does not already exist in the registry
     */
    override fun validateUniqueId(id: String): Boolean {
        return !idCommandMap.containsKey(id)
    }

    /**
     * Gets a command from the registry based on its prefix + alias.
     * If the prefix + alias does not exist in the registry, null is returned.
     */
    override fun getCommandByAlias(alias: String): CommandPackage<*>? {
        val id = aliasIdMap[alias] ?: return null
        return idCommandMap[id]
    }

    /**
     * Gets a command from the registry based on its unique identifier.
     * If the unique identifier does not exist in the registry, null is returned.
     */
    override fun getCommandById(id: String): CommandPackage<*>? {
        return idCommandMap[id]
    }

    /**
     * Returns a list of all root-level commands (i.e., commands with parent identifiers being an empty string "").
     */
    override fun getAllCommands(sortById: Boolean): List<CommandPackage<*>> {
        return if (sortById) {
            idCommandMap.values
                .filter { it.properties.parentId == CommandDefaults.PARENT_ID }
                .sortedBy { it.properties.id }
                .toList()
        } else {
            idCommandMap.values
                .filter { it.properties.parentId == CommandDefaults.PARENT_ID }
                .toList()
        }
    }

    /**
     * Returns a list of all root-level command aliases (prefix + alias).
     */
    override fun getAllCommandAliases(sorted: Boolean): List<String> {
        return if (sorted) {
            aliasIdMap.keys.sorted().toList()
        } else {
            aliasIdMap.keys.toList()
        }
    }

    /**
     * Returns whether or not a parent command has any sub-commands
     */
    override fun hasSubCommands(parentId: String): Boolean {
        return parentIdSubCommandsMap.containsKey(parentId)
    }

    /**
     * Gets the sub-command registry of a given parent command.
     * Returns null if the parent command does not have any sub-commands.
     */
    override fun getSubCommandRegistry(parentId: String): SubCommandRegistry? {
        return parentIdSubCommandsMap[parentId]
    }

    /**
     * Gets the sub-command of a given parent command by the sub-command's alias (prefix + alias).
     * Returns null if no match is found.
     */
    override fun getSubCommandByAlias(alias: String, parentId: String): CommandPackage<*>? {
        val registry = this.getSubCommandRegistry(parentId) ?: return null
        val subCommandId = registry.getSubCommandIdByAlias(alias) ?: return null
        return registry.getSubCommandById(subCommandId)
    }

    /**
     * Adds a command to the registry
     */
    override fun addCommand(command: CommandPackage<*>): Boolean {
        // Check for valid alias
        if (!validateAliases(command.properties.prefix, command.properties.aliases)) {
            LOGGER.error { "Could not register $command due to it clashing with existing aliases in the registry." }
            return false
        } else if (!validateUniqueId(command.properties.id)) {
            LOGGER.error { "Could not register $command due to the unique id already existing in the registry." }
            return false
        }

        // Add command aliases into alias->id map
        command.properties.aliases.forEach {
            aliasIdMap[command.properties.prefix + it] = command.properties.id
        }

        // Add command into id->command map
        idCommandMap[command.properties.id] = command

        LOGGER.debug { "Registered command: $command" }
        return true
    }

    /**
     * Adds a sub-command to a parent command
     */
    override fun addSubCommand(subCommand: CommandPackage<*>, parentId: String): Boolean {
        // TODO: fix parentId in CommandProperties of sub-command if not adding through annotations
        // Update command registry
        val subCommandRegistry = parentIdSubCommandsMap.getOrPut(parentId, { SubCommandRegistryImpl() })
        // Add sub-command
        return subCommandRegistry.addSubCommand(subCommand, parentId)
    }

    /**
     * Removes a sub-command from a parent command
     */
    override fun removeSubCommand(subCommandId: String, parentId: String): Boolean {
        val subCommandRegistry = parentIdSubCommandsMap[parentId] ?: return false
        val success = subCommandRegistry.removeSubCommand(subCommandId)
        if (success && !subCommandRegistry.containsCommands()) {
            parentIdSubCommandsMap.remove(parentId)
        }
        return success
    }

    override fun deleteCommand(id: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun enableCommand(id: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun disableCommand(id: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
