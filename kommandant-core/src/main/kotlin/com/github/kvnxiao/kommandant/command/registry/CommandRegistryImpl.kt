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
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.github.kvnxiao.kommandant.command.registry

import com.github.kvnxiao.kommandant.command.CommandDefaults
import com.github.kvnxiao.kommandant.command.CommandPackage
import mu.KotlinLogging

private val LOGGER = KotlinLogging.logger { }

/**
 * The default command registry implementation used for registering and de-registering commands and sub-commands.
 * When dealing with sub-commands, the registry will use a [SubCommandRegistryImpl] underneath the hood.
 */
open class CommandRegistryImpl : CommandRegistry() {

    /**
     * The map of unique command identifiers to the respective root-level commands.
     */
    protected val idCommandMap: MutableMap<String, CommandPackage<*>> = mutableMapOf()

    /**
     * The root-level map of command aliases to their respective commands (does not contain sub-commands,
     * i.e. commands that are children to this command).
     */
    protected val aliasIdMap: MutableMap<String, String> = mutableMapOf()

    /**
     * The map of parent commands to their respective sub-command registries.
     * The parent command can be either a root-level command, a sub-command of a root-level command, or a sub-command
     * of a sub-command, etc.
     */
    protected val parentIdSubCommandsMap: MutableMap<String, SubCommandRegistry> = mutableMapOf()

    override fun validateAliases(prefix: String, aliases: Set<String>): Boolean {
        return aliases.none { aliasIdMap.containsKey(prefix + it) }
    }

    override fun validateUniqueId(id: String): Boolean {
        return !idCommandMap.containsKey(id)
    }

    override fun getCommandByAlias(alias: String): CommandPackage<*>? {
        val id = aliasIdMap[alias] ?: return null
        return idCommandMap[id]
    }

    override fun getCommandById(id: String): CommandPackage<*>? {
        return idCommandMap[id]
    }

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

    override fun getAllCommandAliases(sorted: Boolean): List<String> {
        return if (sorted) {
            aliasIdMap.keys.sorted().toList()
        } else {
            aliasIdMap.keys.toList()
        }
    }

    override fun hasSubCommands(id: String): Boolean {
        return parentIdSubCommandsMap.containsKey(id)
    }

    override fun getSubCommandRegistry(parentCommandId: String): SubCommandRegistry? {
        return parentIdSubCommandsMap[parentCommandId]
    }

    override fun getSubCommandByAlias(alias: String, parentId: String): CommandPackage<*>? {
        val registry = this.getSubCommandRegistry(parentId) ?: return null
        val subCommandId = registry.getSubCommandIdByAlias(alias) ?: return null
        return registry.getSubCommandById(subCommandId)
    }

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

    override fun addSubCommand(subCommand: CommandPackage<*>, parentId: String): Boolean {
        // Require that the subCommand's parentId and supplied parent id are the same
        if (subCommand.properties.parentId != parentId) {
            LOGGER.warn { "Attempted to add a sub-command to the registry but the parent id property did not match!" +
                "Expected ${subCommand.properties.parentId} from the sub-command's properties, but was looking for $parentId" }
            return false
        }
        // Update command registry
        val subCommandRegistry = parentIdSubCommandsMap.getOrPut(parentId, { SubCommandRegistryImpl() })
        // Add sub-command
        return subCommandRegistry.addSubCommand(subCommand, parentId)
    }

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
