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

import com.github.kvnxiao.kommandant.command.CommandPackage
import mu.KotlinLogging

private val LOGGER = KotlinLogging.logger { }

/**
 * The default sub-command registry implementation used for registering and de-registering sub-commands.
 */
open class SubCommandRegistryImpl : SubCommandRegistry() {

    /**
     * The sub-command-level map of unique command identifiers to the respective commands.
     */
    protected val subIdCommandMap: MutableMap<String, CommandPackage<*>> = mutableMapOf()

    /**
     * The sub-command-level map of command aliases to their respective commands (does not contain further sub-commands,
     * i.e. commands that are children to this command).
     */
    protected val aliasIdMap: MutableMap<String, String> = mutableMapOf()

    override fun validateAliases(prefix: String, aliases: Set<String>): Boolean {
        return aliases.none { aliasIdMap.containsKey(it) }
    }

    override fun validateUniqueId(id: String): Boolean {
        return !subIdCommandMap.containsKey(id)
    }

    override fun getSubCommandById(id: String): CommandPackage<*>? {
        return subIdCommandMap[id]
    }

    override fun getSubCommandIdByAlias(alias: String): String? = aliasIdMap[alias]

    override fun getAllSubCommandIds(sortById: Boolean): List<String> {
        return if (sortById) subIdCommandMap.keys.sorted().toList() else subIdCommandMap.keys.toList()
    }

    override fun getAllSubCommandAliases(sorted: Boolean): List<String> {
        return if (sorted) aliasIdMap.keys.sorted().toList() else aliasIdMap.keys.toList()
    }

    override fun addSubCommand(subCommand: CommandPackage<*>, parentId: String): Boolean {
        // Validate aliases
        if (!validateAliases(aliases = subCommand.properties.aliases)) {
            LOGGER.error { "Could not register sub-command $subCommand to $parentId due to it clashing with existing sub-command aliases." }
            return false
        } else if (!validateUniqueId(subCommand.properties.id)) {
            LOGGER.error { "Could not register sub-command $subCommand to $parentId due to the unique id already existing in the sub-command registry." }
            return false
        }

        // Map all aliases of the sub-command to the sub-command id
        subCommand.properties.aliases.forEach {
            aliasIdMap[it] = subCommand.properties.id
        }
        // Add sub-command to id->command map
        subIdCommandMap[subCommand.properties.id] = subCommand

        LOGGER.debug { "Registered sub-command $subCommand to $parentId" }
        return true
    }

    override fun removeSubCommand(subCommandId: String): Boolean {
        // Return false by default if the provided sub-command id does not exist
        val subCommand = subIdCommandMap[subCommandId] ?: return false
        // Remove all aliases from alias map
        subCommand.properties.aliases.forEach {
            aliasIdMap.remove(it)
        }
        subIdCommandMap.remove(subCommandId)
        return true
    }

    override fun containsCommands(): Boolean {
        return aliasIdMap.isNotEmpty()
    }
}
